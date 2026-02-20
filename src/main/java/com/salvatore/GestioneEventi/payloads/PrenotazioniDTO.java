package com.salvatore.GestioneEventi.payloads;

import jakarta.validation.constraints.NotNull;

public record PrenotazioniDTO(

        @NotNull(message = "è obligatorio l'ID dell'evento per effettuare una prenotazione")
        Long eventoId
) {
}
