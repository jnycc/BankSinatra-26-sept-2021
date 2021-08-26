package com.miw.service.authentication;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.miw.database.JdbcTokenDao;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.security.Key;
import java.util.Base64;

import java.util.Date;
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
    * wordt aangemaakt tijdens het inloggen
    *
    * deel 1: HEADER: algoritme en token type (JWT)
    * deel 2: value/data of the token: bijv naam, rol, id en email, time, (nothing confidential, no password)
    * just enough data of user to verivy who that person is
    * deel 3: Verify signature: (header, payload en secret --> annaloog aan pepper
    * deze info wordt meegegeven vanuit de website
    * en is de signature waaraan de site kan zien dat de JWT legitiem is (crytocraphic hash)
    *
    * Header and payload zijn omgezet in een string aan de hand van Based64 encoded
    * (niet als beveiliging maar voor de convienence) -> voor de server to validate if info is corect.
    * Verify signature: created by server with help of the secret (pepper)
    * is gelinkt aan de header en payload
    *
    * FLow:
    * - user logt in
    * - gegevens worden gecheckt (authenticatie)
    * - jwt wordt gecreeert
    * - jwt wordt terug gestuurd naar de user (local storage of coocie)
    * - With every new user request:
    *       - jwt gaat terug naar de server
    *       - via HTTP HEADER: key/value names: key: Authoraziation, value: Bearer JWT
    *       - server checkt JWT
    *           1. splitsen in drie delen
    *           2. based64 decoding of part 1 en 2
    *           3. calculates signature of this decoding
    *           4. checkes if this calculated signature matches the given signature (part 3)
    *
    * Expiration payload: (little tricky)
    * - Blacklisted JWT --> JWT's that are no longer valid
    * */


    // bij authenticatie wordt token aangemaakt en opgeslagen in aparte tabel in de database
    // bij het doorgaan naar een nieuwe pagina wordt token vanuit de gerbuiker kant doorgegeven (via HTML)
    // doorgegeven token wordt afgezet tegenover token in database ter validatie


    public String generateToken() {
        return UUID.randomUUID().toString();       // TODO: omzetten in jason Web token?
    }

    //Base64.Decoder decoder;
    //return Base64.getUrlEncoder().encodeToString(bytes);


    public String generateJwt(String id, String issuer, String subject, long ttlMillis){

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        //Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //Key signatureKey = "Pepper";

        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                //.signWith(signatureAlgorithm, signingKey)
                ;


        // header en payload omzetten in json

        // header en payload coderen met code64
        //headerEncoded = Base64.getEncoder().encode(header);
        //headerEncoded = Base64.getEncoder().encode(payload);


        // header + payload + pepper coderen met code64 -> signature
        // gecodeerde header + . + payload + . + signature opslaan en returnen


        //jwt = "header" + "." + "payload" + "." + "signature";

        return builder.compact();
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
