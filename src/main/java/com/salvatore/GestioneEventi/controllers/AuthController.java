package com.salvatore.GestioneEventi.controllers;


import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.ValidationException;
import com.salvatore.GestioneEventi.payloads.LoginDTO;
import com.salvatore.GestioneEventi.payloads.LoginResponseDTO;
import com.salvatore.GestioneEventi.payloads.UtenteDTO;
import com.salvatore.GestioneEventi.services.AuthService;
import com.salvatore.GestioneEventi.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService utenteService;

    @Autowired
    public AuthController(AuthService authService, UtenteService utenteService) {
        this.authService = authService;
        this.utenteService = utenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body){

        return new LoginResponseDTO(this.authService.checkCredentialAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUtente(@RequestBody @Validated UtenteDTO payload, BindingResult validationResult){
        if(validationResult.hasErrors()){
            List<String> errorList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorList);
        } else {
            return this.utenteService.saveUtente(payload);
        }
    }
}
