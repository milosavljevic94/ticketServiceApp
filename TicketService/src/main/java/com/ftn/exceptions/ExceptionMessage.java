package com.ftn.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ExceptionMessage {

    private String m;
    private HttpStatus status;
    private LocalDateTime time;

    public ExceptionMessage(String m, HttpStatus status) {
        this.m = m;
        this.status = status;
        this.time = LocalDateTime.now();
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
