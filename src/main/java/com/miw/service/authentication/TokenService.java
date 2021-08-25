package com.miw.service.authentication;
import java.util.UUID;

// instantie maken van de TokenDao

public class TokenService {

    // bij authenticatie wordt token aangemaakt en in combinatie met email opgeslagen in aparte tabel in de database
    // bij het doorgaan naar een nieuwe pagina wordt token vanuit de gerbuiker kant doorgegeven (via HTML)
    // doorgegeven token wordt afgezet tegenover token in database ter validatie


    public String generateToken() {
        return UUID.randomUUID().toString();       // TODO: omzetten in jason Web token?
    }

    public boolean validateToken(String token) {
        return checkToken(token) != null;
    }

    public void expireToken(String token) {
        deleteToken(token);
    }



    // TODO: methode creeren die token meegeeft via de header --> IN HTML


}
