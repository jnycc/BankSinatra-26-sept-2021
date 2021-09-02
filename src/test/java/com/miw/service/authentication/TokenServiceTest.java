package com.miw.service.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TokenServiceTest {

    TokenService tokenService = new TokenService();

    @Test
    void jwtBuilder() {
        // Creating future date
        Date futureDate = new Date(2022, Calendar.JANUARY, 1, 0, 10, 10);
        long futureMilSec = futureDate.getTime();

        // assert equals
        String actualJwt = tokenService.jwtBuilderSetDate("test@testen.nl",
                futureMilSec, 600000).toString(); //10 min
        String expectedJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJpYXQiOjYxNTk5MTM2MjEwLCJzdWIiOiJ0ZXN0QHRlc3Rlbi5ubCIsImV4cCI6NjE1OTkxMzY4MTB9" +
                ".MYUFnQnRlIdPP579jRRprgzOgYzJPm5mmBhNZdhLmgk";
        assertEquals(expectedJwt, actualJwt);
    }

    @Test
    void jwtExpired() {
        // Create current date
        Date today = new Date(System.currentTimeMillis());
        long todayMilSec = today.getTime();

        // create jwt with expiration date of zero
        String expiredJwt = tokenService.jwtBuilderSetDate("test@testen.nl",todayMilSec, 0); //0 min
        System.out.println(expiredJwt);

        // assert equals
        Boolean actual = tokenService.decodeJWTBool(expiredJwt);
        Boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    void jwtNotExpired() {
        // Create current date
        Date today = new Date(System.currentTimeMillis());
        long todayMilSec = today.getTime();

        // create jwt with expiration date of 10 minutes in the future
        String validJwt = tokenService.jwtBuilderSetDate("test@testen.nl",todayMilSec, 600000); //10 min

        // assert equals
        Boolean actual = tokenService.decodeJWTBool(validJwt);
        Boolean expected = true;
        assertEquals(expected, actual);
    }


    @Test
    void decodeJwt() {
        // Creating future date
        Date futureDate = new Date(2022, Calendar.JANUARY, 1, 0, 10, 10);
        long futureMilSec = futureDate.getTime();

        // creating JWT for test use
        String testJwt = tokenService.jwtBuilderSetDate("test@testen.nl", futureMilSec, 600000).toString(); //10 min

        // assert equals for decoded jwt
        String actualClaim = tokenService.decodeJWT(testJwt).toString();
        String expectedClaim = "{iat=61599136210, sub=test@testen.nl, exp=61599136810}";
        assertEquals(expectedClaim, actualClaim);
    }


    @Test
    public void decodeExpiredjwt() {
        // Creating future date
        Date futureDate = new Date(2022, Calendar.JANUARY, 1, 0, 10, 10);
        long futureMilSec = futureDate.getTime();

        // creating JWT for test use
        String testJwt = tokenService.jwtBuilderSetDate("test@testen.nl", futureMilSec, 0).toString(); //0 min

        // assert equals for decoded jwt
        String actualClaim = tokenService.decodeJWT(testJwt).toString();
        String expectedClaim = "{iat=61599136210, sub=test@testen.nl, exp=61599136210}";
        assertEquals(expectedClaim, actualClaim);
    }

}