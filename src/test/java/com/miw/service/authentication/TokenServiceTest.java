package com.miw.service.authentication;

import com.miw.model.Client;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TokenServiceTest {

    // set up valid jwt with future date
    private String setUpValidJWT(){
        Date futureDate = new Date(2025, Calendar.JANUARY, 1, 0, 10, 10);
        long futureMilSec = futureDate.getTime();
        return TokenService.jwtBuilderSetDate("test@testen.nl", "admin",
                futureMilSec, 600000); //10 min
    }

    // Set up invalid jwt with expirationdate of zero.
    private String setUpInvalidJWT(){
        return TokenService.jwtBuilderSetDate("test@testen.nl", "admin",
                new Date().getTime(), 0); //0 min
    }


    @Test
    void jwtBuilder() {
        String actualJwt = setUpValidJWT();
        String expectedJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJSb2xlIjoiYWRtaW4iLCJzdWIiOiJ0ZXN0QHRlc3Rlbi5ubCIsImV4cCI6NjE2OTM4MzEyMTAsImlhdCI6NjE2OTM4MzA2MTB9.DxF1alK-MJ12JT7TYuxhRi6F8o_ZMhKZ5GfKVS_VoHw";
        assertEquals(expectedJwt, actualJwt);
    }

    @Test
    void decodeJWTBoolExpired() {
        Boolean actual = TokenService.decodeJWTBool(setUpInvalidJWT());
        Boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    void decodeJWTBoolValid() {
        Boolean actual = TokenService.decodeJWTBool(setUpValidJWT());
        Boolean expected = true;
        assertEquals(expected, actual);
    }


    @Test
    void decodeJwt() {
        String actualClaim = TokenService.decodeJWT(setUpValidJWT()).toString();
        String expectedClaim = "{Role=admin, sub=test@testen.nl, exp=61693831210, iat=61693830610}";
        assertEquals(expectedClaim, actualClaim);
    }

    @Test
    void decodeJwt2() {
        String actualClaim = (TokenService.decodeJWT(setUpValidJWT())).toString();
        String expectedClaim = "{Role=admin, sub=test@testen.nl, exp=61693831210, iat=61693830610}";
        assertEquals(expectedClaim, actualClaim);
    }

    @Test
    void GetEmailJWT() {
        String actual = TokenService.validateAndGetEmailJWT(setUpValidJWT().toString());
        String expected = "test@testen.nl";
        assertEquals(actual, expected);
    }

    @Test
    void GetEmailExpiredJWT() {
        String actual = TokenService.validateAndGetEmailJWT(setUpInvalidJWT());
        String expected = null;
        assertEquals(actual, expected);
    }


//    @Test
//    public void decodeExpiredjwt() {
//        String actualClaim = TokenService.decodeJWT(setUpInvalidJWT()).toString();
//        String expectedClaim = ExpiredJwtException;
//        assertEquals(expectedClaim, actualClaim);
//    }

}