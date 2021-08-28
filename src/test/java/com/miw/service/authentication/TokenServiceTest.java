package com.miw.service.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TokenServiceTest {

    ByteArrayToHexHelper byteArrayToHexHelper = new ByteArrayToHexHelper();
    TokenService tokenService = new TokenService();

    @Test
    void generateJwt() {
        String actualJwt = tokenService.generateJwt("123", "test@testen.nl", 100000); //10 min
        System.out.println(actualJwt);

        Claims actualClaim = tokenService.decodeJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJpYXQiOjE2MzAxNTgyODYsInN1YiI6InRlc3RAdGVzdGVuLm5sIiwiZXhwIjoxNjMwMTU4Mzg2fQ.mycF1TgysfbYdGyu9ayBmBnA91PtW_7zBylkP95qN2s");
        System.out.println(actualClaim);


        //TODO: expected jwt string toevoegen die rekening houdt met meegegeven tijd
        String expectedJwt = "";
        //assertEquals(actualJwt, expectedJwt);
    }


    @Test
    public void decodeJWT(String jwt) {
        //TODO: fixen van waarom onderstaande niet werkt..
//       Claims expectedClaim = new ;
//        expectedClaim.put("jti", 123);
//        expectedClaim.put("iat", 1630153047);
//        expectedClaim.put("subject", "subject");
//        expectedClaim.put("iss", "issuer");
//        expectedClaim.put("exp", 1630153647);
//       System.out.println(expectedClaim);


//        Claims actualClaim = tokenService.decodeJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJpYXQiOjE2MzAxNTQ4NzMsInN1YiI6InN1YmplY3QiLCJpc3MiOiJpc3N1ZXIifQ.G186f5H_aLbRFGPpAUmrNe9vWO2cWIToVfVq29dX7NY");
//        System.out.println(actualClaim);
//        assertEquals(actualClaim, expectedClaim);


    }
}