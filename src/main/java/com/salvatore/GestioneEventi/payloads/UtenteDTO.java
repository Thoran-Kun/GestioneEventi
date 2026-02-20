package com.salvatore.GestioneEventi.payloads;

import com.salvatore.GestioneEventi.entities.RuoloUtente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UtenteDTO(
        @NotBlank(message = "Lo username è obbligatorio")
        @Size(min = 2, max = 30, message = "Lo username deve rientrare tra i 2 e i 30 caratteri")
        String username,

        @NotBlank(message = "La mail è obbligatoria")
        @Email(message = "Email non valida")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$",
                message = "La password deve contenere almeno una maiuscola, una minuscola e un numero")
        String password,

        @NotNull(message = "Scegli il ruolo (UTENTE_NORMALE o ORGANIZZATORE) in fase di registrazione")
        RuoloUtente ruolo
) {
}