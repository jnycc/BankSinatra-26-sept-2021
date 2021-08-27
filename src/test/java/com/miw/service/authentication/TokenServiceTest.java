package com.miw.service.authentication;

import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;



class TokenServiceTest {

    @Test
    void generateJwt() {
        TokenService tokenService = new TokenService();
        String jwt = tokenService.generateJwt("123","issuer", "subject");
        System.out.println(jwt);

//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("banaan");
//        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//        System.out.println(Arrays.toString(signingKey.getEncoded()));
    }


}