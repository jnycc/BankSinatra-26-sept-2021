package com.miw.service.authentication;
/**
 * @Author: Nijad Nazarli
 * @Description: This service class authenticates the user
 *               based on the details entered while logging in
 */
import com.miw.database.JdbcAdminDao;
import com.miw.database.JdbcTokenDao;
import com.miw.database.JdbcClientDao;
import com.miw.model.Administrator;
import com.miw.model.Client;
import com.miw.model.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private HashService hashService;
    private JdbcClientDao clientDao;
    private TokenService tokenService;
    private JdbcTokenDao jdbcTokenDao;
    private JdbcAdminDao adminDao;
    private final String INVALID_CREDENTIALS = "Invalid credentials";
    private final String BLOCKED_USER = "User is blocked";

    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    public AuthenticationService(HashService hs, JdbcClientDao clientDao, TokenService tokenService,
                                 JdbcTokenDao jdbcTokenDao, JdbcAdminDao adminDao) {
        super();
        this.hashService = hs;
        this.clientDao = clientDao;
        this.tokenService = tokenService;
        this.jdbcTokenDao = jdbcTokenDao;
        this.adminDao = adminDao;
        logger.info("New AuthenticationService created");
    }

    public String authenticate(Credentials credentials) {
        Client clientDatabase = clientDao.findByEmail(credentials.getEmail());
        Client clientLogIn = new Client(credentials.getEmail(), credentials.getPassword());

        if (clientDatabase != null) {
            clientLogIn.setSalt(clientDatabase.getSalt());
            String hash = hashService.hashForAuthenticate(clientLogIn).getPassword();

            if (clientDatabase.getPassword().equals(hash)) {
                if (clientDatabase.isBlocked()) {
                    return BLOCKED_USER;
                }
                return TokenService.jwtBuilder(credentials.getEmail(), credentials.getClass().toString(), 7400000); //2 uur geldig
            }
            return INVALID_CREDENTIALS;
        }
        return INVALID_CREDENTIALS;
    }

    public String authenticateAdmin(Credentials credentials) {
        Administrator adminDatabase = adminDao.findByEmail(credentials.getEmail());
        Administrator adminLogIn = new Administrator(credentials.getEmail(), credentials.getPassword());

        if (adminDatabase != null) {
            adminLogIn.setSalt(adminDatabase.getSalt());
            String hash = hashService.hashForAuthenticate(adminLogIn).getPassword();

            if (adminDatabase.getPassword().equals(hash)) {
                if (adminDatabase.isBlocked()) {
                    return BLOCKED_USER;
                }
                return TokenService.jwtBuilder(credentials.getEmail().toString(), credentials.getClass().toString(),7400000); //2 uur geldig
            }
            return INVALID_CREDENTIALS;
        }
        return INVALID_CREDENTIALS;
    }

    public HashService getHashService() {
        return hashService;
    }

    public JdbcClientDao getClientDao() {
        return clientDao;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public JdbcTokenDao getJdbcTokenDao() {
        return jdbcTokenDao;
    }

    public String getINVALID_CREDENTIALS() {
        return INVALID_CREDENTIALS;
    }

    public String getBLOCKED_USER() {
        return BLOCKED_USER;
    }
}
