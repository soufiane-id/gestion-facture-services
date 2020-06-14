package ma.sid.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class JSONExceptionResponse {

    private Date timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public JSONExceptionResponse(Date timestamp, HttpStatus status, String message, List<String> errors) {
        super();
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public JSONExceptionResponse(Date timestamp, HttpStatus status, String message, String error) {
        super();
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

}