package com.chisimdi.Ecommerce.exceptions;

import java.time.Instant;

public class ApiError {

    private int status;
    private String error;
    private Instant localDateTime;

    public ApiError(int status, String error) {
        this.status = status;
        this.error = error;
        this.localDateTime = Instant.now();
    }

    public int getStatus() {
        return status;
    }

    public Instant getLocalDateTime() {
        return localDateTime;
    }

    public String getError() {
        return error;
    }
}


