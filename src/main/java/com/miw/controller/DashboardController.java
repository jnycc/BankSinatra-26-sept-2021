package com.miw.controller;

import com.miw.database.JdbcAccountDao;
import com.miw.database.JdbcAssetDao;
import com.miw.model.Asset;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
        int ID = TokenService.getUserID(token);
        return jdbcAccountDao.getAccountByUserID(ID).getBalance();
    }

    //TODO: getAssetsBuserID() toevoegen aan assetsDAO
    @PostMapping("/getPortfolioValue")
    public double getPortfolioValue(@RequestBody String token) {
        int ID = TokenService.getUserID(token);
        List<Asset> clientAssets = new ArrayList<Asset>();
        clientAssets = jdbcAssetDao.getAssets(ID);
        double totalValue = 0.0;
        for (Asset asset: clientAssets) {
            totalValue += asset.getUnits() * asset.getCurrentValue();
        }
        return totalValue;
    }





}


