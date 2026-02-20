package com.salvatore.GestioneEventi.controllers;

import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.ValidationException;
import com.salvatore.GestioneEventi.payloads.UtenteDTO;
import com.salvatore.GestioneEventi.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

   @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente saveUtente(@RequestBody @Validated UtenteDTO payload, BindingResult validationResult){
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.utenteService.saveUtente(payload);
        }
   }

   @GetMapping("/me")
    public Utente detUtente(@AuthenticationPrincipal Utente currentAuthenticatedUtente){
        return currentAuthenticatedUtente;
   }

   @GetMapping("/{utenteId}")
   @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'USER')")
    public Utente getUtenteById(@PathVariable long utenteId){
        return this.utenteService.findById(utenteId);
   }
}
