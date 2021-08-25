package com.miw.service.authentication;

import com.miw.database.RootRepository;
import com.miw.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private RootRepository rootRepository;

    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    public AuthenticationService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;
        logger.info("New AuthenticationService created");
    }

}
