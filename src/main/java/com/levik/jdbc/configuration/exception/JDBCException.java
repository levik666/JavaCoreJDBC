package com.levik.jdbc.configuration.exception;

public class JDBCException extends RuntimeException{

    public JDBCException(String message) {
        super(message);
    }

    public JDBCException(String message, Throwable cause) {
        super(message, cause);
    }
}
