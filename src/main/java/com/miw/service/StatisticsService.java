package com.miw.service;

import com.miw.database.JdbcAccountDao;
import com.miw.database.JdbcTransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class StatisticsService {

    private final PortfolioService portfolioService;
    private final JdbcTransactionDao jdbcTransactionDao;
    private final JdbcAccountDao jdbcAccountDao;


    @Autowired
    public StatisticsService(JdbcTransactionDao jdbcTransactionDao,
                             PortfolioService portfolioService,
                             JdbcAccountDao jdbcAccountDao) {
        this.jdbcTransactionDao = jdbcTransactionDao;
        this.portfolioService = portfolioService;
        this.jdbcAccountDao = jdbcAccountDao;
    }


    // get portfoliovalue on every date since daysBack
    public Map<LocalDate, Double> getPortfolioStats(int userID, int daysBack) {
        Map<LocalDate, Double> portfolioValues = new TreeMap<>();
        int accountID = jdbcAccountDao.getAccountByUserID(userID).getAccountId();
        //iterate through days to get cumulative values
        for (int days = daysBack; days >= 0; days--) {
            double portfolioValue = jdbcTransactionDao.getPortfolioValueByDate(accountID, days);
            LocalDate date = LocalDate.now().minusDays(days);
            portfolioValues.put(date,portfolioValue);
        }
        return portfolioValues;
    }

    // get portfolio value increase/decrease in percentage
    public double getPercentageIncrease(int userID, int daysBack){
        int accountID = jdbcAccountDao.getAccountByUserID(userID).getAccountId();
        double pastVal = jdbcTransactionDao.getPortfolioValueByDate(accountID, daysBack);
        double currentVal = portfolioService.getTotalPortfolioValue(userID);
        return ((currentVal/pastVal)*100)-100;
    }

}
