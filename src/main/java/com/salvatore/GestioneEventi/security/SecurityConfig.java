package com.salvatore.GestioneEventi.security;

import org.springframework.beans.factory.BeanRegistrarDslMarker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        //disabilito comportamenti di default come autenticazione di form
        //1. STEP DI INIZIALIZZAZIONE APP
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        //2.
        httpSecurity.csrf(csrf -> csrf.disable());
        //3.
        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //4.PERSONALIZZAZIONE DI COMPORTAMENTI PRE-ESISTENTI
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());

        // Disabilitiamo il meccanismo di default di Spring Security che ritorna 401 ad ogni richiesta.
        // Siccome andremo ad implementare un meccanismo di autenticazione custom, sarà il nostro metodo di autenticazione e controllo a proteggere
        // i vari endpoint, non Security direttamente

        // - aggiungere ulteriori funzionalità custom

        httpSecurity.cors(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getBCrypt(){
        return new BCryptPasswordEncoder(13);
    }
    }


