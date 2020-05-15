package com.ftn.exceptions;

import com.ftn.project.TicketServiceApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsHandlers extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER =LoggerFactory.getLogger(TicketServiceApplication.class);

    @ExceptionHandler(value = {
            EntityNotFoundException.class,
            LocationNotFoundException.class
    })
    public ResponseEntity<ExceptionMessage> entityNotFoundExeptionHandler(Exception e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage(), status);
        LOGGER.error(exceptionMessage.getM());
        return new ResponseEntity<>(exceptionMessage, status);
    }

    @ExceptionHandler(value = {
            EntityAlreadyExistException.class,
            EmailExistsException.class
    })
    public ResponseEntity<ExceptionMessage> entityAlreadyExistExceptionHandler(Exception e){
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage(), status);
        LOGGER.error(exceptionMessage.getM());
        return new ResponseEntity<>(exceptionMessage, status);
    }

    @ExceptionHandler(value = DateException.class)
    public ResponseEntity<ExceptionMessage> wrongDateExceptionHandler(DateException e){

        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage(), status);
        LOGGER.error(exceptionMessage.getM());
        return new ResponseEntity<>(exceptionMessage, status);
    }

    @ExceptionHandler(value = SectorIsFullException.class)
    public ResponseEntity<ExceptionMessage> fullSectorExceptionHandler(SectorIsFullException e){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage(), status);
        LOGGER.error(exceptionMessage.getM());
        return new ResponseEntity<>(exceptionMessage, status);
    }

    @ExceptionHandler(value = SeatIsNotFreeException.class)
    public ResponseEntity<ExceptionMessage> seatNotFreeExceptionHandler(SeatIsNotFreeException e){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage(), status);
        LOGGER.error(exceptionMessage.getM());
        return new ResponseEntity<>(exceptionMessage, status);
    }

    @ExceptionHandler(value = AplicationException.class)
    public ResponseEntity<ExceptionMessage> applicationExceptionsExceptionHandler(AplicationException e){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionMessage exceptionMessage = new ExceptionMessage(e.getMessage(), status);
        LOGGER.error(exceptionMessage.getM());
        return new ResponseEntity<>(exceptionMessage, status);
    }

    /*
    Spring has built in handleMethodArgumentException then override it.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        LOGGER.error(errors.toString());
        return new ResponseEntity<>(body, status);
    }
}
