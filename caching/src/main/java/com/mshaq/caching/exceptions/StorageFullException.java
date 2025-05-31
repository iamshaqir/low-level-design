package com.mshaq.caching.exceptions;

public class StorageFullException extends RuntimeException{

    public StorageFullException(String message) {
        super(message);
    }
}
