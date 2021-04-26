package ch.uzh.ifi.hase.soprafs21.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotCreatorException extends RuntimeException {
    public NotCreatorException(String message){
        super(message);
    }
}
