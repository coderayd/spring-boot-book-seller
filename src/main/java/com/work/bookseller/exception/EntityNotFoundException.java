package com.work.bookseller.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message){ super(message); }
}
