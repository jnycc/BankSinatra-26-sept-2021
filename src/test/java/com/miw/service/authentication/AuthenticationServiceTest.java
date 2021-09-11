package com.miw.service.authentication;

import com.miw.model.Credentials;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AuthenticationServiceTest {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceTest.class);
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceTest(AuthenticationService as) {
        super();
        this.authenticationService = as;
        logger.info("New AuthenticationService Integration Test");
    }

    @Test
    void integrationTest() {
        assertThat(authenticationService.getHashService()).isNotNull();
    }

    @Test
    void authenticateTest() {
        Credentials validCredentials = new Credentials("test@test.com", "zeerveiligwachtwoord2");
        Credentials noArgsCredentials = new Credentials();
        Credentials oneMoreCredentials = new Credentials("an@gmail.com", "WWwoord123");
        Credentials notAnEmail = new Credentials("dd", "PASSWORD123");
        Credentials shortPassword = new Credentials("nn@gmail.com", "11");

        assertThat(authenticationService.authenticate(validCredentials)).isNotEmpty();
        assertThat(authenticationService.authenticate(noArgsCredentials)).isEqualTo(authenticationService.getINVALID_CREDENTIALS());
        assertThat(authenticationService.authenticate(oneMoreCredentials)).isEqualTo(authenticationService.getINVALID_CREDENTIALS());
        assertThat(authenticationService.authenticate(notAnEmail)).isEqualTo(authenticationService.getINVALID_CREDENTIALS());
        assertThat(authenticationService.authenticate(shortPassword)).isEqualTo(authenticationService.getINVALID_CREDENTIALS());


    }
}