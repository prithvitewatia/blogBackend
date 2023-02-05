package dev.prithvis.blogbackend.exceptions;

/**
 * Exception class for unauthorized access
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(){
        super();
    }
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message,Throwable cause){
        super(message, cause);
    }
}
