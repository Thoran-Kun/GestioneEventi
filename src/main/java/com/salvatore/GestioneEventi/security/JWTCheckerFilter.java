package com.salvatore.GestioneEventi.security;

import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.UnauthorizedException;
import com.salvatore.GestioneEventi.services.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private UtenteService utenteService;

    public JWTCheckerFilter(JWTTools jwtTools, UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //-------------------------------- AUTENTICAZIONE -------------------------------
        //VERIFICO CHE LA RICHIESTA CONTIENE L'HEADER E CHE SIA IN FORMATO BEARER
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'Authorization header in formato corretto");

        //  ESTRAGGO IL TOKEN DALL'HEADER
        String accessToken = authHeader.replace("Bearer ", "");

        // VERIFICO SE IL TOKEN è VALIDO
        jwtTools.verifyToken(accessToken);

        //-------------------------------- AUTORIZZAZIONE ----------------------------------
        // 1. Cerchiamo l'utente nel DB tramite id (l'id sta nel token!)
        // 1.1 Leggiamo l'id dal token
        Long utenteId = jwtTools.extractIdFromToken(accessToken);
        // 1.2 Find by Id
        Utente authenticatedUtente = this.utenteService.findById(utenteId);

        // 2. Associamo l'utente al Security Context
        // E' uno step fondamentale che serve per associare l'utente che sta effettuando la richiesta (il proprietario del token) alla richiesta
        // stessa per tutta la sua durata, cioè fino a che essa non ottiene una risposta
        // Così facendo chiunque arriverà dopo questo filtro, altri filtri, il controller, un endpoint... potrà risalire a chi sia l'utente che
        // ha effettuato la richiesta
        // Questo è molto utile per ad esempio controllare i ruoli dell'utente prima di arrivare ad uno specifico endpoint. Oppure ci può servire
        // per effettuare determinati controlli all'interno degli endpoint stessi per verificare che chi stia facendo una certa operazione di
        // lettura/modifica/cancellazione sia l'effettivo proprietario della risorsa, oppure per, in fase di creazione di una risorsa, associare
        // l'effettivo proprietario a tale risorsa.
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUtente, null, authenticatedUtente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //-------------------------PARTE FINALE DEL PROCEDIMENTO----------------------------
        filterChain.doFilter(request, response);
        // se ci sono problemi lancio eccezione
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
    }

