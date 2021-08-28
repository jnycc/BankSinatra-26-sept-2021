package com.miw.service.authentication;
import com.miw.database.JdbcTokenDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import javax.management.relation.Role;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

//TODO: constructor en autowire @service etc.

@Service
public class TokenService {

    private static String jwt;
    private JdbcTokenDao jdbcTokenDao;

    @Autowired
    public TokenService(JdbcTokenDao jdbcTokenDao) {
        this.jdbcTokenDao = jdbcTokenDao;
    }

    public TokenService() {
    }

    // Generating and validating jwt

    public String generateJwt(String id, String userEmail, long ttlMillis){

        //JWT signature algorithm:
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date dateIssued = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("pepper");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //set JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(dateIssued)
                .setSubject(userEmail)
                //TODO: voeg rol toe in payload.
                //.setClaims("roles", jdbcUserDao.getRoleByEmail(userEmail))
                .signWith(signatureAlgorithm, signingKey);

        //add expiration date and time
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
           builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("pepper"))
                .parseClaimsJws(jwt).getBody();
    }



    // Generating and validating UUID token:

    public String generateToken() {
        return UUID.randomUUID().toString();       // TODO: omzetten in jason Web token?
    }

    public boolean validateToken(String token) {
        // TODO: checken op datum?
        return jdbcTokenDao.retrieveToken(token) != null;
    }

    // TODO: methode creeren die token meegeeft via de header --> IN HTML


}


///////         NOTES           //////



//Base64.Decoder decoder;
//return Base64.getUrlEncoder().encodeToString(bytes);

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
 * (niet als beveiliging maar voor de convienence) -> voor de server to validate if info is correct.
 * Verify signature: created by server with help of the secret (pepper)
 * is gelinkt aan de header en payload
 *
 * FLow:
 * - user logt in
 * - gegevens worden gecheckt (authenticatie)
 * - jwt wordt gecreeert
 * - jwt wordt terug gestuurd naar de user (local storage of cookie)
 * - With every new user request:
 *       - jwt gaat terug naar de server
 *       - via HTTP HEADER: key/value names: key: Authoraziation, value: Bearer JWT
 *       - server checkt JWT
 *           1. splitsen in drie delen
 *           2. based64 decoding of part 1 en 2
 *           3. calculates signature of this decoding
 *           4. checks if this calculated signature matches the given signature (part 3)
 *
 * Expiration payload: (little tricky)
 * - Blacklisted JWT --> JWT's that are no longer valid
 * */

// header en payload omzetten in json