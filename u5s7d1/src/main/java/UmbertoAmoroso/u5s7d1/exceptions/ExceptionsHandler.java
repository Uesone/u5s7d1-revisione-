package UmbertoAmoroso.u5s7d1.exceptions;



import UmbertoAmoroso.u5s7d1.payloads.ErrorsResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsPayload handleUnauthorizedException(UnauthorizedException ex) {
        return new ErrorsPayload(ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsResponseDTO handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorsResponseDTO("Non hai i permessi per accedere", LocalDateTime.now());
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsPayload handleNotFoundException(NotFoundException ex) {
        return new ErrorsPayload(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleBadRequestException(BadRequestException ex) {
        return new ErrorsPayload(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorsPayload errors = new ErrorsPayload("Errore di validazione");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.addError(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsPayload handleGenericException(Exception ex) {
        return new ErrorsPayload("Errore interno del server: " + ex.getMessage());
    }
}

