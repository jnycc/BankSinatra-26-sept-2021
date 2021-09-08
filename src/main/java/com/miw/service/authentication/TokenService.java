package com.miw.service.authentication;
import com.miw.database.JdbcAdminDao;
import com.miw.database.JdbcClientDao;
import com.miw.database.JdbcTokenDao;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//TODO: constructor en autowire @service etc.

@Service
public class TokenService {
    private JdbcTokenDao jdbcTokenDao;
    private JdbcAdminDao jdbcAdminDao;
    private JdbcClientDao jdbcClientDao;

    @Autowired
    public TokenService(JdbcTokenDao jdbcTokenDao, JdbcClientDao jdbcClientDao, JdbcAdminDao jdbcAdminDao) {
        this.jdbcTokenDao = jdbcTokenDao;
        this.jdbcAdminDao = jdbcAdminDao;
        this.jdbcClientDao = jdbcClientDao;
    }

    public TokenService() {
    }


    public static String jwtBuilder(int userID, String role, long expTime){ // input: Role role (nieuwe klasse Role?)
        //generating secret key for JWT signature
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(new PepperService().getPepper());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

        // creating specific claims to add
        Map<String, Object> tokenClaims = new HashMap<>();
        tokenClaims.put("userrole", role);

        //set JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(tokenClaims)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(String.valueOf(userID))
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(SignatureAlgorithm.HS256, signingKey);

        //Building JWT set to compact, URL-safe string
        return builder.compact();
    }

    //TODO: clean up code (super() of same method above?)
    public static String jwtBuilderSetDate(int userID, String role, long msNow, long expTime){
        //generating secret key for JWT signature
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(new PepperService().getPepper());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

        // creating specific claims to add
        Map<String, Object> tokenClaims = new HashMap<>();
        tokenClaims.put("userrole", role);

        //set JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(tokenClaims)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(msNow))
                .setSubject(String.valueOf(userID))
                .setExpiration(new Date(msNow + expTime))
                .signWith(SignatureAlgorithm.HS256, signingKey);

        //Building JWT set to compact, URL-safe string
        return builder.compact();
    }

    //Will throw an exception JWT is expired or invalid
    public static Claims decodeJWT(String jwt) {
        //TODO: splitsen op spatie en Bearer weghalen
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(new PepperService().getPepper()))
                .parseClaimsJws(jwt).getBody();
    }

    // Will return userID if JWT is valid.
    public static Integer validateAndGetID(String jwt) {
        try {
            return Integer.valueOf(decodeJWT(jwt).getSubject());
        } catch (ExpiredJwtException expired) {
            return 0;
        }
    }

    public static Boolean decodeJWTBool(String jwt) {
        try {
            decodeJWT(jwt);
        } catch (ExpiredJwtException expired) {
            // TODO: checken of refreshmenttoken nog geldig is?
            return false;
        }
        return true;
    }


    //TODO: implement refreshtoken?
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public boolean validateRefreshToken(String token) {
        return jdbcTokenDao.retrieveToken(token) != null;
    }

} // end of main


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