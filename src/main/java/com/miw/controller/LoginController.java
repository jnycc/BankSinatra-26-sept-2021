package com.miw.controller;
/**
 * @Author: Nijad Nazarli
 * @Description: This controller enables users to Login to their account
 */
import com.google.gson.Gson;
import com.miw.database.JdbcClientDao;
import com.miw.model.Credentials;
import com.miw.service.authentication.AuthenticationService;
import com.miw.service.authentication.TokenService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;


@RestController
@Validated
public class LoginController {

    private AuthenticationService authenticationService;
    private TokenService tokenService;
    private JdbcClientDao jdbcClientDao;
    private Gson gson;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AuthenticationService authenticationService, TokenService ts, JdbcClientDao jdbcClientDao, Gson gson) {
        super();
        this.authenticationService = authenticationService;
        this.tokenService = ts;
        this.jdbcClientDao = jdbcClientDao;
        this.gson = gson;
        logger.info("New LoginController Created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody String credentialsAsJson) {
        @Valid Credentials credentials = gson.fromJson(credentialsAsJson, Credentials.class);
        String response = authenticationService.authenticate(credentials);
        return showLoginResponse(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody String credentialsAsJson) {
        @Valid Credentials credentials = gson.fromJson(credentialsAsJson, Credentials.class);
        String response = authenticationService.authenticateAdmin(credentials);
        return showLoginResponse(response);
    }

    // Naar dashboardController?
    @PostMapping("/authenticate")
    public boolean authenticate(@RequestBody String token) {
        return TokenService.decodeJWTBool(token);
    }

    // Naar dashboardController?
    @PostMapping("/getEmail")
    public String getEmail(@RequestBody String token) {
        return TokenService.validateAndGetEmailJWT(token);
    }

    public ResponseEntity<?> showLoginResponse(String response) {
        if (response.equals(authenticationService.getINVALID_CREDENTIALS())){
            return new ResponseEntity<>(authenticationService.getINVALID_CREDENTIALS(), HttpStatus.UNAUTHORIZED);
        } else if (response.equals(authenticationService.getBLOCKED_USER())) {
            return new ResponseEntity<>(authenticationService.getBLOCKED_USER(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // TODO Eventueel deze methode hieruit halen, voorbeeld van resource ophalen met token
    @GetMapping("/gegevens/{email}")
    public ResponseEntity<?> showMyData(@RequestHeader("Authorization") String token, @PathVariable("email") @Email String email) {
        //Claims claims = tokenService.decodeJwt

        if (TokenService.decodeJWTBool(token)) {
            return ResponseEntity.ok(jdbcClientDao.findByEmail(email));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
