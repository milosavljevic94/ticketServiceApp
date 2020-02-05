package com.ftn.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String m) {
        super(m);
    }
}
