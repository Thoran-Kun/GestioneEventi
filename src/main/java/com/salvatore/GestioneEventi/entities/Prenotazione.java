package com.salvatore.GestioneEventi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name="evento_id", nullable = false)
    private Evento evento;

    private LocalDateTime dataPrenotazione = LocalDateTime.now();

    public Prenotazione(long id, Utente utente, Evento evento, LocalDateTime dataPrenotazione) {
        this.id = id;
        this.utente = utente;
        this.evento = evento;
        this.dataPrenotazione = dataPrenotazione;
    }
}
