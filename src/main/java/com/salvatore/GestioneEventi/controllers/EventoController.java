package com.salvatore.GestioneEventi.controllers;

import com.salvatore.GestioneEventi.entities.Evento;
import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.ValidationException;
import com.salvatore.GestioneEventi.payloads.EventoDTO;
import com.salvatore.GestioneEventi.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Evento saveEvento(@RequestBody @Validated EventoDTO payload,
                             BindingResult validationResult, @AuthenticationPrincipal Utente organizzatore){
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.eventoService.saveEvento(payload, organizzatore);
        }
    }

    //trovo tutti gli eventi disponibili
    @GetMapping
    public List<Evento> getAllEvent(){
        return eventoService.findAll();
    }

    @GetMapping("/{eventoId}")
    public Evento getEventoById(@PathVariable long eventoId){
        return eventoService.findById(eventoId);
    }

    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento updateEvento(@PathVariable long eventoId, @RequestBody @Validated EventoDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return eventoService.findByIdAndUpdate(eventoId, body);
    }

    @DeleteMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable long eventoId) {
        eventoService.findByIdAndDelete(eventoId);
    }
}
