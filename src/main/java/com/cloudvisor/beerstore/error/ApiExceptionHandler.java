package com.cloudvisor.beerstore.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.cloudvisor.beerstore.service.exception.BusinessException;
import com.cloudvisor.beerstore.error.ErrorResponse.ApiError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private static final String NO_MESSAGE_AVAILABLE = "No message available";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageSource apiErrorMessageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception, Locale locale) {
        Stream<ObjectError> errors = exception.getBindingResult().getAllErrors().stream();
        List<ApiError> apiErrors = errors
                .map(ObjectError::getDefaultMessage)
                .map(this::createErrorCode)
                .map(code -> toApiError(code, locale))
                .collect(toList());

        return ResponseEntity.badRequest().body(ErrorResponse.ofErrors(HttpStatus.BAD_REQUEST, apiErrors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(BusinessException exception, Locale locale) {
        final ErrorCode errorCode = createErrorCode(exception.getCode(), exception.getStatus());
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), toApiError(errorCode, locale));
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception, Locale locale) {
        final ErrorCode errorCode = createErrorCode("beerType.invalid", HttpStatus.BAD_REQUEST);
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), toApiError(errorCode, locale, exception.getValue()));
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception, Locale locale) {
        LOGGER.error("Error not expected", exception);
        final ErrorCode errorCode = createErrorCode("internalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), toApiError(errorCode, locale));
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }


    private ApiError toApiError(ErrorCode errorCode, Locale locale, Object... args) {
        String message;
        try {
            message = apiErrorMessageSource.getMessage(errorCode.getCode(), args, locale);
        } catch (NoSuchMessageException e) {
            LOGGER.error("Couldn't find any message for {} code under {} locale", errorCode.getCode(), locale);
            message = NO_MESSAGE_AVAILABLE;
        }

        return new ApiError(errorCode.getCode(), message);
    }

    private ErrorCode createErrorCode(final String errorCode, final HttpStatus status) {
        return new ErrorCode(errorCode, status);
    }

    private ErrorCode createErrorCode(final String errorCode) {
        return createErrorCode(errorCode, HttpStatus.BAD_REQUEST);
    }

}