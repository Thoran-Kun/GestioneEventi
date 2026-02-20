package com.salvatore.GestioneEventi.services;

import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.UnauthorizedException;
import com.salvatore.GestioneEventi.payloads.LoginDTO;
import com.salvatore.GestioneEventi.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteService utenteService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

   @Autowired
    public AuthService(UtenteService utenteService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialAndGenerateToken(LoginDTO body){
        //1. controllo le credenziali e se esiste un dipendente con quella mail
        Utente found = this.utenteService.findByEmail(body.email());

        //2. se esiste controllo che la password del DB è uguale a quella del body
        // AGGIORNO IL METODO PER CONFRONTARE LE PASSWORD PRESENTE DAL DB E QUELLO DELL'UTENTE
        if(bcrypt.matches(body.password(), found.getPassword())){
            //2.01 se credenziali OK
            //2.02 genero il token
            String accessToken = jwtTools.generateToken(found);
            // ritorno infine il token
            return accessToken;
        } else {
            throw new UnauthorizedException("Le tue credenziali sono errate, riprova!");
        }
    }
}
