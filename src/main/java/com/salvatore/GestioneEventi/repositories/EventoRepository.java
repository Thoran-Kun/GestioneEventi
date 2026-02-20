package com.salvatore.GestioneEventi.repositories;

import com.salvatore.GestioneEventi.entities.Evento;
import com.salvatore.GestioneEventi.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByOrganizzatore(Utente organizzatore);
}
