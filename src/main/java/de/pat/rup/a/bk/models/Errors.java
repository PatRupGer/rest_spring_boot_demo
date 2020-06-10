package de.pat.rup.a.bk.models;

import java.util.ArrayList;
import java.util.List;

public class Errors {
    private final List<Error> errors;

    public Errors() {
        errors = new ArrayList<>();
    }

    public List<Error> getErrors() {
        return errors;
    }
}

