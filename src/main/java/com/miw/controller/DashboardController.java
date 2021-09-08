package com.miw.controller;

import com.miw.database.JdbcAccountDao;
import com.miw.database.JdbcAssetDao;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private TokenService tokenService;
    private JdbcAccountDao jdbcAccountDao;
    private JdbcAssetDao jdbcAssetDao;

    @Autowired
    public DashboardController() {
        super();
        logger.info("New DashboardController created");
    }

    @GetMapping("/dashboard")
    void something(){
        logger.info("something");
    }

    @PostMapping("/getBalance")
    public double getBalance(@RequestBody String token) {
        int ID = TokenService.GetUserID(token);
        return jdbcAccountDao.getAccountByUserID(ID).getBalance();
    }

    //TODO: getAssetsByuserID() toevoegen aan assetsDAO
//    @PostMapping("/getPortfolioValue")
//    public double getPortfolioVallue(@RequestBody String token) {
//        int ID = TokenService.validateAndGetID(token);
//        return jdbcAssetDao.getAssetsByuserID();
//    }





}


