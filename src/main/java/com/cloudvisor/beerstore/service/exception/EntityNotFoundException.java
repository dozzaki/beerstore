package com.cloudvisor.beerstore.service.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super("entityNotFound", HttpStatus.NOT_FOUND);
    }

}