package com.chisimdi.Ecommerce.exceptions;

import java.time.Instant;
import java.util.ArrayList;

public class ApiError2 {
    private int status;
    private String message;
    private Instant localDateTime;
    private ArrayList<String> error=new ArrayList<>();

    public ApiError2(int status,String message){
        this.status=status;
        this.message=message;
        this.localDateTime=Instant.now();
    }

    public Instant getLocalDateTime() {
        return localDateTime;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getError() {
        return error;
    }



}
