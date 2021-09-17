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
    private final JdbcAssetDao jdbcAssetDao;
    private final JdbcTransactionDao jdbcTransactionDao;
    private final JdbcCryptoDao jdbcCryptoDao;

    @Autowired
    public StatisticsService(JdbcAssetDao jdbcAssetDao, JdbcTransactionDao jdbcTransactionDao, JdbcCryptoDao jdbcCryptoDao) {
        this.jdbcAssetDao = jdbcAssetDao;
        this.jdbcTransactionDao = jdbcTransactionDao;
        this.jdbcCryptoDao = jdbcCryptoDao;
    }



    // TODO: Method to get list of crypto values with certain interval (base case: each day of the last week):
    // DAO Method can imeadiatly be integrated in endpoint?

    // TODO: Method to get list of total portfolio value with certain interval (base case: each day of the last week):
    public Map<LocalDate, Double> getPortfolioStats(int userID){
        Map<LocalDate, Double > totalPortfolioValue = new HashMap<>();
        Map<LocalDate, Map<String, Double>> courseValues = jdbcCryptoDao.getPricesPerDate();
        Map<LocalDate, Map<String, Double>> boughtUnits = jdbcTransactionDao.getBoughtUnitsPerDay(userID);
        Map<LocalDate, Map<String, Double>> soldUnits = jdbcTransactionDao.getSoldUnitsPerDay(userID);

        // retrieves the first date that units where bought:

        var startdate = boughtUnits.keySet().stream().min(Comparator.comparing(LocalDate::toEpochDay)).orElse(LocalDate.now());
        int portfolioDays = (int) ChronoUnit.DAYS.between(startdate, LocalDate.now());

        // Iterate over each date since portfolio has units, starting with today
        // TODO: fixen als er op 1 datum geen transacties zijn -> TRY CATCH??? TO STREAM??
            for (int days = 0; days <= portfolioDays; days++) {
                double portfolioValue = 0.0;
                LocalDate dayTeller = LocalDate.now().minusDays(days);
                // Iterate over each crypto that portfolio has units of and add to total portfolio value
                for (String symbol : boughtUnits.get(dayTeller).keySet()) {
                    double cryptoValue = courseValues.get(dayTeller).get(symbol) * boughtUnits.get(dayTeller).get(symbol);
                    portfolioValue += cryptoValue;
                }
                // inbouwen: als op die datum niets gekocht/verkocht is = error voorkomen
                for (String symbol : soldUnits.get(dayTeller).keySet()) {
                    double cryptoValue = courseValues.get(dayTeller).get(symbol) * soldUnits.get(dayTeller).get(symbol);
                    portfolioValue -= cryptoValue;
                }
                totalPortfolioValue.put(dayTeller,portfolioValue);
            }
        return totalPortfolioValue;
    }

}
