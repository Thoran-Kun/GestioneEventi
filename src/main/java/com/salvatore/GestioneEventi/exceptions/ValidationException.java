package com.salvatore.GestioneEventi.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errorsMessages;
    public ValidationException(List<String> errorsMessages) {

        super("ATTENZIONE! Errori nel payload");
        this.errorsMessages = errorsMessages;
    }
    }

