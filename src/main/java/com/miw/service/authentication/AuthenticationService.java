package com.miw.service.authentication;

import com.miw.database.JdbcUserDao;
import com.miw.database.RootRepository;
import com.miw.database.UserDao;
import com.miw.model.Client;
import com.miw.model.User;
import com.miw.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private RootRepository rootRepository;
    private HashService hashService;
    private JdbcUserDao userDao;

    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    public AuthenticationService(RootRepository rootRepository, HashService hs, JdbcUserDao userDao) {
        super();
        this.rootRepository = rootRepository;
        this.hashService = hs;
        this.userDao = userDao;
        logger.info("New AuthenticationService created");
    }

    public boolean authenticate(String email, String password) {
        Client clientDatabase = userDao.findByEmail(email);
        if (clientDatabase == null) {
            return false;
        } else  {
            String hash = hashService.hashForAuthenticate(password, clientDatabase.getSalt());
            return hash.equals(clientDatabase.getPassword());
        }
    }

}
