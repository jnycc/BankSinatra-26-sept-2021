package com.miw.service.authentication;

import com.miw.database.JdbcTokenDao;
import com.miw.database.JdbcUserDao;
import com.miw.database.RootRepository;
import com.miw.model.Client;
import com.miw.model.Credentials;
import com.miw.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private HashService hashService;
    private JdbcUserDao userDao;
    private TokenService tokenService;
    private JdbcTokenDao jdbcTokenDao;

    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    public AuthenticationService(HashService hs, JdbcUserDao userDao, TokenService tokenService, JdbcTokenDao jdbcTokenDao) {
        super();
        this.hashService = hs;
        this.userDao = userDao;
        this.tokenService = tokenService;
        this.jdbcTokenDao = jdbcTokenDao;
        logger.info("New AuthenticationService created");
    }

    public String authenticate(Credentials credentials) {
        Client clientDatabase = userDao.findByEmail(credentials.getEmail());
        Client clientLogIn = new Client(credentials.getEmail(), credentials.getPassword());
        String hash = "";

        if (clientDatabase != null) {
            clientLogIn.setSalt(clientDatabase.getSalt());
            hash = hashService.hashForAuthenticate(clientLogIn).getPassword();

            if (clientDatabase.getPassword().equals(hash)) {
                String token = tokenService.generateToken();
                jdbcTokenDao.saveToken(token);
                logger.info(token);
                return token;
            }
        }
        return "";
    }



}
