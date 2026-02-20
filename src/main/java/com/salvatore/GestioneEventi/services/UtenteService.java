package com.salvatore.GestioneEventi.services;

import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.BadRequestException;
import com.salvatore.GestioneEventi.exceptions.NotFoundException;
import com.salvatore.GestioneEventi.payloads.UtenteDTO;
import com.salvatore.GestioneEventi.repositories.UtenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder bcrypt;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.bcrypt = passwordEncoder;
    }

    //salvo l'utente nel DB
    public Utente saveUtente(UtenteDTO payload) {
        utenteRepository.findByEmail(payload.email()).ifPresent(user -> {
            throw new BadRequestException("la mail inserita: " + payload.email() + " è già in uso!");
        });

        Utente utente = new Utente();
        utente.setUsername(payload.username());
        utente.setEmail(payload.email());
        utente.setPassword(bcrypt.encode(payload.password()));
        utente.setRuolo(payload.ruolo());
        Utente saveUtente = utenteRepository.save(utente);
        log.info("l'utente " + saveUtente.getUsername() + " è stato salvato correttamente!");
        return  saveUtente;
    }

    public Utente findByEmail(String email){
        return utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    public Utente findById(long id){
        return utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("utente con ID: " + id + " non è stato trovato!"))
    }
}
