package com.salvatore.GestioneEventi.services;

import com.salvatore.GestioneEventi.entities.Evento;
import com.salvatore.GestioneEventi.entities.Prenotazione;
import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.BadRequestException;
import com.salvatore.GestioneEventi.exceptions.NotFoundException;
import com.salvatore.GestioneEventi.payloads.PrenotazioniDTO;
import com.salvatore.GestioneEventi.repositories.PrenotazioneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final EventoService eventoService;

    @Autowired
    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, EventoService eventoService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.eventoService = eventoService;
    }

    public Prenotazione savePrenotazione(PrenotazioniDTO payload, Utente utente){
        Evento evento = eventoService.findById(payload.eventoId());

        //controllo se il mio utente ha già prenotato questo evento grazie ad un boolean avrò true o false
        if(prenotazioneRepository.existsByUtenteAndEvento(utente, evento)){
            throw new BadRequestException("Hai già prenotato per questo evento: " + evento.getTitolo());
        }

        //controllo se ho ancora posti disponibili
        long prenotazioniTotali = prenotazioneRepository.countByEvento(evento);
        if(prenotazioniTotali >= evento.getPostiDisponibili()) {
            throw new BadRequestException("Posti esauriti per l'evento: " + evento.getTitolo());
        }

        //se passo tutti questi controlli creo l'evento
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setEvento(evento);
        prenotazione.setDataPrenotazione(LocalDateTime.now());

        Prenotazione savedPrenotazione = prenotazioneRepository.save(prenotazione);
        log.info("Prenotazione per evento: " + evento.getTitolo() + " a nome dell'utente: " + utente.getUsername() + " è stata creata correttamente!");
        return savedPrenotazione;
    }

    public Prenotazione findById(long id) {
        return prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione con ID " + id + " non trovata"));
    }

    public List<Prenotazione> findByUtente(Utente utente) {
        return prenotazioneRepository.findByUtente(utente);
    }

    public void deletePrenotazione(long id, Utente utente) {
        Prenotazione found = this.findById(id);
        if(found.getUtente().getId() != utente.getId()){
            throw new BadRequestException("puoi cancellare solo la tua prenotazione");
        }
        prenotazioneRepository.delete(found);
    }
}
