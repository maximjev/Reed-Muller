package reed.muller.encoding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EncodingException extends RuntimeException {
    public EncodingException() {
    }

    public EncodingException(String message) {
        super(message);
    }
}
