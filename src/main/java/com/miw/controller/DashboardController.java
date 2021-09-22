package com.miw.controller;

import com.google.gson.Gson;
import com.miw.database.JdbcAccountDao;
import com.miw.database.JdbcAssetDao;
import com.miw.database.RootRepository;
import com.miw.model.Client;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private TokenService tokenService;
    private JdbcAccountDao jdbcAccountDao;
    private JdbcAssetDao jdbcAssetDao;
    private RootRepository rootRepository;

    @Autowired
    public DashboardController(TokenService tokenService, JdbcAccountDao jdbcAccountDao, JdbcAssetDao jdbcAssetDao, RootRepository rootRepository) {
        super();
        this.tokenService = tokenService;
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcAssetDao = jdbcAssetDao;
        this.rootRepository = rootRepository;
        logger.info("New DashboardController created");
    }

    //TODO: waarom komt token niet binnen???
    //TODO: dit moet een GetMapping worden. Data wordt ge-get, resource op server wordt niet aangepast.
    @PostMapping("/getBalance")
    public double getBalance(@RequestBody String token) {
        int ID = TokenService.getValidUserID(token);
        try {
            //double balance = jdbcAccountDao.getAccountByUserID(ID).getBalance() +9.78204;
            double balance = Math.round((jdbcAccountDao.getAccountByUserID(ID).getBalance())*100);
            return balance/100;
        } catch (NullPointerException fault) {
            return 0.00;
        }
//        double balance = jdbcAccountDao.getAccountByUserID(ID).getBalance();
//        balance = Math.round(balance*100);
//        return balance/100;
    }


//    @PostMapping("/getPortfolioValue")
//    public double getPortfolioValue(@RequestBody String token) {
//        int ID = TokenService.getValidUserID(token);
//        List<Asset> clientAssets = jdbcAssetDao.getAssets(ID);
//        double totalValue = 0.00;
//        for (Asset asset: clientAssets) {
//            totalValue += asset.getUnits() * asset.getCurrentValue();
//        }
//        return totalValue;
//    }

    @GetMapping("/getNameClient")
    public ResponseEntity<?> getNameByUserId(@RequestHeader("Authorization") String token){
        int userId = TokenService.getValidUserID(token);
        if(userId == 0){
            return new ResponseEntity<>("Not a valid Token", HttpStatus.UNAUTHORIZED);
        } else {
            String name = rootRepository.getFirstNameById(userId);
            return new ResponseEntity<>(name, HttpStatus.OK);
        }
    }



}


