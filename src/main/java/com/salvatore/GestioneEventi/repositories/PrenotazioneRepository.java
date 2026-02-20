package com.salvatore.GestioneEventi.repositories;

import com.salvatore.GestioneEventi.entities.Evento;
import com.salvatore.GestioneEventi.entities.Prenotazione;
import com.salvatore.GestioneEventi.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findByUtente(Utente utente);

    boolean existsByUtenteAndEvento(Utente utente, Evento evento);

    long countByEvento(Evento evento);
}
