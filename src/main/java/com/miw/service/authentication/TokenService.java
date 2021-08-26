package com.miw.service.authentication;
import com.miw.database.JdbcTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

//TODO: constructor en autowire @service etc.

@Service
public class TokenService {

    private final JdbcTokenDao jdbcTokenDao;


    @Autowired
    public TokenService(JdbcTokenDao jdbcTokenDao) {
        this.jdbcTokenDao = jdbcTokenDao;
    }


    /*
    * JWT:
    * token is een string bestaande uit 3 delen (gescheiden door een punt -> .)
    * deel 1: HEADER: algoritme en token type (JWT)
    * deel 2: user data: bijv naam, id en email
    * deel 3: Verify signature: deze info wordt meegegeven vanuit de website
    * en is de signature waaraan de site kan zien dat de JWT legitiem is
    *
    * Deze informatie is omgezet in een onleesbare string (o.a. aan de hand van de pepper)
    *
    *
    *
    *
    * */


    // bij authenticatie wordt token aangemaakt en opgeslagen in aparte tabel in de database
    // bij het doorgaan naar een nieuwe pagina wordt token vanuit de gerbuiker kant doorgegeven (via HTML)
    // doorgegeven token wordt afgezet tegenover token in database ter validatie


    public String generateToken() {
        return UUID.randomUUID().toString();       // TODO: omzetten in jason Web token?
    }

    public boolean validateToken(String token) {
        // TODO: checken op datum
        return jdbcTokenDao.retrieveToken(token) != null;
    }

/*    public void expireToken(String token) {
        deleteToken(token);
    }*/


    // TODO: methode creeren die token meegeeft via de header --> IN HTML


}
