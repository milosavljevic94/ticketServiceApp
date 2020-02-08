package com.ftn.exceptions;

public class SeatIsNotFreeException extends RuntimeException {
    public SeatIsNotFreeException(String m) {
        super(m);
    }
}
