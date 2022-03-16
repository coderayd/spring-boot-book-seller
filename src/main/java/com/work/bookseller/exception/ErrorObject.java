package com.work.bookseller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorObject {

    protected Integer statusCode;
    protected Date timestamp;
    protected String message;
    protected String description;
}

class ApiError extends ErrorObject{

    private List<String> errors;

    public ApiError(int statusCode, Date timestamp, String message, String description, List<String> errors) {
        super(statusCode,timestamp,message,description);
        this.errors = errors;
    }
}
