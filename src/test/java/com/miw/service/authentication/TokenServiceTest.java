package com.miw.service.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TokenServiceTest {

    ByteArrayToHexHelper byteArrayToHexHelper = new ByteArrayToHexHelper();
    TokenService tokenService = new TokenService();

    @Test
    void jwtBuilder() {
        String actualJwt = tokenService.jwtBuilder("123", "test@testen.nl", 10000000); //10 min



        System.out.println(actualJwt);

        String actualClaim = tokenService.decodeJWT(actualJwt).toString();
        String expected = "{jti=123, iat=1630414071, sub=test@testen.nl, exp=1630424071}";


        JwsHeader jwtHeader = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("pepper"))
                .parseClaimsJws(actualJwt).getHeader();

        String Id = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("pepper"))
                .parseClaimsJws(actualJwt).getBody().getId().toString();

        String userEmail = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("pepper"))
                .parseClaimsJws(actualJwt).getBody().getSubject().toString();


        System.out.println(jwtHeader);

        System.out.println("actualClaims = " + actualClaim);


        //TODO: expected jwt string toevoegen die rekening houdt met meegegeven tijd
        //String expectedJwt = "";
        //assertEquals(actualJwt, expectedJwt);
    }


    @Test
    public void decodeJWT() {
        //TODO: fixen van waarom onderstaande niet werkt..
        String jwtExample = "iOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJpYXQiOjE2MzA0MTMyODcsInN1YiI6InRlc3RAdGVzdGVuLm5sIiwiZXhwIjoxNjMwNDIzMjg3fQ.p5116Zlqjwg-HW-Qvj9RB3geZtSnkHJ5Ddb354bwZSk";

        Claims actualClaim = tokenService.decodeJWT("iOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJpYXQiOjE2MzA0MTMyODcsInN1YiI6InRlc3RAdGVzdGVuLm5sIiwiZXhwIjoxNjMwNDIzMjg3fQ.p5116Zlqjwg-HW-Qvj9RB3geZtSnkHJ5Ddb354bwZSk");
        String expectedCLaim = "header={alg=HS256},body={jti=123, iat=1630413287, sub=test@testen.nl, " +
                "exp=1630423287},signature=p5116Zlqjwg-HW-Qvj9RB3geZtSnkHJ5Ddb354bwZSk";

        //assertEquals(actualClaim, expectedCLaim);




//        Claims actualClaim = tokenService.decodeJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJpYXQiOjE2MzAxNTQ4NzMsInN1YiI6InN1YmplY3QiLCJpc3MiOiJpc3N1ZXIifQ.G186f5H_aLbRFGPpAUmrNe9vWO2cWIToVfVq29dX7NY");
//        System.out.println(actualClaim);
//


    }
}