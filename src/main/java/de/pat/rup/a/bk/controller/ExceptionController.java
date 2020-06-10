package de.pat.rup.a.bk.controller;

import de.pat.rup.a.bk.models.Error;
import de.pat.rup.a.bk.models.Errors;
import de.pat.rup.a.bk.util.BKException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BKException.class)
    public ResponseEntity<Errors> customHandleNotFound(Exception ex, WebRequest request) {
        Errors errors = new Errors();
        Error err = new Error(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage());
        errors.getErrors().add(err);

        return new ResponseEntity<Errors>(errors, HttpStatus.BAD_REQUEST);

    }
}
