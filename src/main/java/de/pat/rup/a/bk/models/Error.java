package de.pat.rup.a.bk.models;

import org.springframework.http.HttpStatus;

public class Error {
    private final int status;
    private final HttpStatus title;
    private final String description;

    public Error(int status, HttpStatus httpStatus, String description) {
        this.status = status;
        this.title = httpStatus;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}