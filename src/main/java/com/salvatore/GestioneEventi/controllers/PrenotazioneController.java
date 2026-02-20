package com.salvatore.GestioneEventi.controllers;

import com.salvatore.GestioneEventi.entities.Prenotazione;
import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.payloads.PrenotazioniDTO;
import com.salvatore.GestioneEventi.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    @Autowired
    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'ORGANIZZATORE')")
    public Prenotazione createPrenotazione(@RequestBody @Validated PrenotazioniDTO payload, @AuthenticationPrincipal Utente utente){
        return prenotazioneService.savePrenotazione(payload, utente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrenotazione(@PathVariable long id, @AuthenticationPrincipal Utente utente) {
        prenotazioneService.deletePrenotazione(id, utente);
    }
}
