package ch.uzh.ifi.hase.soprafs21.exceptions;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MissingInformationException extends RuntimeException {
    public MissingInformationException(String message){
        super(message);
    }
}
