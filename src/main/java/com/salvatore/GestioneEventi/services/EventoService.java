package com.salvatore.GestioneEventi.services;

import com.salvatore.GestioneEventi.entities.Evento;
import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.NotFoundException;
import com.salvatore.GestioneEventi.payloads.EventoDTO;
import com.salvatore.GestioneEventi.repositories.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventoService {
    private final EventoRepository eventoRepository;

    @Autowired
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento saveEvento(EventoDTO payload, Utente organizzatore){
        Evento evento = new Evento();
        evento.setTitolo(payload.titolo());
        evento.setDescrizione(payload.descrizione());
        evento.setDataEvento(payload.dataEvento());
        evento.setLuogo(payload.luogo());
        evento.setPostiDisponibili(payload.postiDisponibili());
        evento.setOrganizzatore(organizzatore);

        Evento savedEvento = eventoRepository.save(evento);
        log.info("l'evento con id: " + savedEvento.getId() + " è stato salvato correttamente!");
        return savedEvento;
    }

    public Evento findById(long id){
        return eventoRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public Evento findByIdAndUpdate(long id, EventoDTO body) {
        Evento found = this.findById(id);

        found.setTitolo(body.titolo());
        found.setDescrizione(body.descrizione());
        found.setDataEvento(body.dataEvento());
        found.setLuogo(body.luogo());
        found.setPostiDisponibili(body.postiDisponibili());

        return eventoRepository.save(found);
    }

    public void findByIdAndDelete(long id) {
        Evento found = this.findById(id);
        this.eventoRepository.delete(found);
    }
}
