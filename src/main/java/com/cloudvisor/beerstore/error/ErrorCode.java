package com.cloudvisor.beerstore.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ErrorCode {

    private final String code;

    private final HttpStatus httpStatus;
}