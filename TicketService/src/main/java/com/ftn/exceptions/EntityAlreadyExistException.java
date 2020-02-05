package com.ftn.exceptions;

public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(String m) {
        super(m);
    }
}
