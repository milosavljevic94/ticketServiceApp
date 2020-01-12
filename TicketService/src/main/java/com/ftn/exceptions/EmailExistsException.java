package com.ftn.exceptions;

import org.springframework.http.HttpStatus;

public class EmailExistsException extends Exception {
	private HttpStatus status;
    private String message;
    
    public EmailExistsException(){
    	
    }
    
    public EmailExistsException(String message) {
        this.message = message;
    }
    
    public EmailExistsException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
