package de.pat.rup.a.bk.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Something maleformed")
public class BKException extends Exception {
    public BKException(String message) {
        super(message);
    }

}
