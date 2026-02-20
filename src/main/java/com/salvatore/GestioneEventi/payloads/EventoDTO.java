package com.salvatore.GestioneEventi.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventoDTO(
        @NotBlank(message = "inserire titolo evento")
        String titolo,

        @NotBlank(message = "inserire descrizione evento")
        String descrizione,

        @NotNull(message = "inserire la data dell'evento")
        @Future(message = "la data deve essere nel futuro")//Metodo per non poter inserire date nel passato
        LocalDateTime dataEvento,

        @NotBlank(message = "specificare il luogo ")
        String luogo,

        @Min(value = 1, message = "l'evento deve avere obbligatoriamente UN posto disponibile")
        int postiDisponibili
) {
}
