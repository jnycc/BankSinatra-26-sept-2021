package com.miw.service;

import com.miw.database.JdbcAssetDao;
import com.miw.database.JdbcCryptoDao;
import com.miw.database.JdbcTransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StatisticsService {

    private final JdbcTransactionDao jdbcTransactionDao;


    @Autowired
    public StatisticsService(JdbcTransactionDao jdbcTransactionDao) {
        this.jdbcTransactionDao = jdbcTransactionDao;

    }


    public Map<LocalDate, Double> getPortfolioStats(int userID, int daysBack) {
        Map<LocalDate, Double> portfolioValues = new TreeMap<>();

        //iterate through days  
        for (int days = daysBack; days >= 0; days--) {
            double portfolioValue = jdbcTransactionDao.getPortfolioValueByDate(userID, days);
            LocalDate date = LocalDate.now().minusDays(days);
            portfolioValues.put(date,portfolioValue);
        }
        return portfolioValues;
    }

    //TODO: portfolio waarde tenopzichte van vorige dag/week/maand
}
