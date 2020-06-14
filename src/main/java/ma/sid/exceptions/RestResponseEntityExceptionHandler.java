package ma.sid.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { DataIntegrityViolationException.class})
    public ResponseEntity<Object> constraintViolationException(DataIntegrityViolationException ex) {
        JSONExceptionResponse apiError =
                new JSONExceptionResponse(new Date(), HttpStatus.BAD_REQUEST, "Violation de la contrainte d\'unicité : Vérifier que l\'enregistrement n\'existe pas déjà", ex.getLocalizedMessage());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

}