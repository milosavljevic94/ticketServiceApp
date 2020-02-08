package com.ftn.exceptions;

public class SectorIsFullException extends RuntimeException {
    public SectorIsFullException(String m) {
        super(m);
    }
}
