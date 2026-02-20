package com.salvatore.GestioneEventi.exceptions;

import org.springframework.http.HttpStatus;
import com.salvatore.GestioneEventi.payloads.ErrorDTO;
import com.salvatore.GestioneEventi.payloads.ErrorsWithListDTO;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsWithListDTO handleBadRequest(BadRequestException ex){

        return new ErrorsWithListDTO(ex.getMessage(), LocalDateTime.now(), null);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsWithListDTO handleNotFound(NotFoundException ex){
        return new ErrorsWithListDTO(ex.getMessage(), LocalDateTime.now(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsWithListDTO handleValidation(MethodArgumentNotValidException ex) {
        List<String> errorsList = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        return new ErrorsWithListDTO("Errori durante la validazione", LocalDateTime.now(), errorsList);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorsWithListDTO handleGenericServerError(Exception ex){
        ex.printStackTrace();
        return new ErrorsWithListDTO("Errore interno del server", LocalDateTime.now(), null);
    }

    //ERROE SULLE AUTORIZZAZIONI
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //401
    public ErrorDTO handleUnauthorized(UnauthorizedException ex){
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    //CATTURO UN ALTRO ERRORE SULLE AUTORIZZAZIONI
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleForbidden(AuthorizationDeniedException ex){
        return new ErrorDTO("Non hai i permessi per accedere!", LocalDateTime.now());
    }
}
