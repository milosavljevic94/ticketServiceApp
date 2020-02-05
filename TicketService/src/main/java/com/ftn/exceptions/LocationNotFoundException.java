package com.ftn.exceptions;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String m) {
        super(m);
    }
}
