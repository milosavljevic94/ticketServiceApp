package com.ftn.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandlers {

    @ExceptionHandler(value = {
            EntityNotFoundException.class,
            LocationNotFoundException.class
    })
    public ResponseEntity<ExceptionMessage> entityNotFoundExeptionHandler(Exception e){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionMessage exeptionMessage = new ExceptionMessage(e.getMessage(), status);
        return new ResponseEntity<>(exeptionMessage, status);
    }

    @ExceptionHandler(value = {
            EntityAlreadyExistException.class,
            EmailExistsException.class
    })
    public ResponseEntity<ExceptionMessage> entityAlreadyExistExceptionHandler(Exception e){

        HttpStatus status = HttpStatus.ALREADY_REPORTED;
        ExceptionMessage exeptionMessage = new ExceptionMessage(e.getMessage(), status);
        return new ResponseEntity<>(exeptionMessage, status);
    }

    @ExceptionHandler(value = DateException.class)
    public ResponseEntity<ExceptionMessage> wrongDateExceptionHandler(Exception e){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionMessage exeptionMessage = new ExceptionMessage(e.getMessage(), status);
        return new ResponseEntity<>(exeptionMessage, status);
    }

    @ExceptionHandler(value = SectorIsFullException.class)
    public ResponseEntity<ExceptionMessage> fullSectorExceptionHandler(Exception e){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionMessage exeptionMessage = new ExceptionMessage(e.getMessage(), status);
        return new ResponseEntity<>(exeptionMessage, status);
    }

    @ExceptionHandler(value = SeatIsNotFreeException.class)
    public ResponseEntity<ExceptionMessage> seatNotFreeExceptionHandler(Exception e){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionMessage exeptionMessage = new ExceptionMessage(e.getMessage(), status);
        return new ResponseEntity<>(exeptionMessage, status);
    }

    @ExceptionHandler(value = AplicationException.class)
    public ResponseEntity<ExceptionMessage> aplicationExceptionsExceptionHandler(Exception e){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionMessage exeptionMessage = new ExceptionMessage(e.getMessage(), status);
        return new ResponseEntity<>(exeptionMessage, status);
    }
}
