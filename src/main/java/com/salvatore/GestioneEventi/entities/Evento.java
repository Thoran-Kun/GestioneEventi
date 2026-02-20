package com.salvatore.GestioneEventi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventi")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataEvento;
    private String luogo;
    private int postiDisponibili;

    @ManyToOne
    @JoinColumn(name = "organizzatore_id", nullable = false)
    private Utente organizzatore;

    public Evento(long id, String titolo, String descrizione, LocalDateTime dataEvento, String luogo, int postiDisponibili, Utente organizzatore) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataEvento = dataEvento;
        this.luogo = luogo;
        this.postiDisponibili = postiDisponibili;
        this.organizzatore = organizzatore;
    }
}
