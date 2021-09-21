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
    // DAO Method is immediately integrated in endpoint.


    // TODO: Methode die in loop alle portfolio waarde ophaalt
    public Map<LocalDate, Double> getPortfolioStats(int userID, int daysBack) {
        Map<LocalDate, Double> portfolioValues = new HashMap<>();

        //iterate through days  
        for (int days = daysBack; days >= 0; days--) {
            double portfolioValue = jdbcTransactionDao.getPortfolioValueByDate(days, userID);
            LocalDate date = LocalDate.now().minusDays(days);
            portfolioValues.put(date,portfolioValue);
        }
        return portfolioValues;
    }


}


//
//    // cumulativeDayValues map gets overwritten, temp Map does'nt work, what to do??!
//    public Map<LocalDate, Map<String, Double>> getCumillitivePortfolioValues(int userID) {
//        Map<String, Double> cumulativeDayValues = new HashMap<>();
//        Map<LocalDate, Map<String, Double>> totalCumulativeValues = new HashMap<>();
//        Map<LocalDate, Map<String, Double>> boughtUnits = jdbcTransactionDao.getBoughtUnitsPerDay(userID);
//        Map<LocalDate, Map<String, Double>> soldUnits = jdbcTransactionDao.getSoldUnitsPerDay(userID);
//
//// find first day of purchase
//        var startdate = boughtUnits.keySet().stream().min(Comparator.comparing(LocalDate::toEpochDay)).orElse(LocalDate.now());
//        int daysBack = (int) ChronoUnit.DAYS.between(startdate, LocalDate.now());
//
//        // Iterate over each date since portfolio has units, starting with first day of crypto purchase
//        for (int days = daysBack; days >= 0; days--) {
//
//            LocalDate date = LocalDate.now().minusDays(days);
//
//            // If units where bought on this day --> add units
//            if (boughtUnits.containsKey(date)) {
//                for (String symbol : boughtUnits.get(date).keySet()) {
//                    double units = boughtUnits.get(date).get(symbol);
//                    double pastUnits = cumulativeDayValues.getOrDefault(symbol, 0.0);
//
//                    // adds crypto symbol, today value and past values to map
//                    cumulativeDayValues.put(symbol, units + pastUnits);
//                }
//            }
//            // Put everything in super map with current date
//            totalCumulativeValues.put(date, cumulativeDayValues);
//        }
//        return totalCumulativeValues;
//    }
//
//
//}


// NOTES

//    public Map<LocalDate, Double> getPortfolioStatsTrial(int userID){
//        Map<LocalDate, Double > totalPortfolioValue = new HashMap<>();
//        Map<LocalDate, Map<String, Double>> courseValues = jdbcCryptoDao.getPricesPerDate();
//        Map<LocalDate, Map<String, Double>> boughtUnits = jdbcTransactionDao.getBoughtUnitsPerDay(userID);
//        Map<LocalDate, Map<String, Double>> soldUnits = jdbcTransactionDao.getSoldUnitsPerDay(userID);
//
//        var startdate = boughtUnits.keySet().stream().min(Comparator.comparing(LocalDate::toEpochDay)).orElse(LocalDate.now());
//        int portfolioDays = (int) ChronoUnit.DAYS.between(startdate, LocalDate.now());
//
//        // Iterate over each date since portfolio has units, starting with today
//        // TODO: fixen als er op 1 datum geen transacties zijn -> TRY CATCH??? TO STREAM??
//        for (int days = 0; days <= portfolioDays; days++) {
//            double portfolioValue = 0.0;
//            LocalDate dayTeller = LocalDate.now().minusDays(days);
//            // Iterate over each crypto that portfolio has units of and add to total portfolio value
//            for (String symbol : boughtUnits.get(dayTeller).keySet()) {
//                double cryptoValue = courseValues.get(dayTeller).get(symbol) * boughtUnits.get(dayTeller).get(symbol);
//                portfolioValue += cryptoValue;
//            }
//            // inbouwen: als op die datum niets gekocht/verkocht is = error voorkomen
//            for (String symbol : soldUnits.get(dayTeller).keySet()) {
//                double cryptoValue = courseValues.get(dayTeller).get(symbol) * soldUnits.get(dayTeller).get(symbol);
//                portfolioValue -= cryptoValue;
//            }
//            totalPortfolioValue.put(dayTeller,portfolioValue);
//        }
//        return totalPortfolioValue;
//    }
//
//    // TODO: Method to get list of total portfolio value with certain interval (base case: each day of the last week):
//    public Map<LocalDate, Double> getPortfolioStats(int userID){
//        Map<LocalDate, Double > totalPortfolioValue = new HashMap<>();
//        Map<LocalDate, Map<String, Double>> courseValues = jdbcCryptoDao.getPricesPerDate();
//        Map<LocalDate, Map<String, Double>> boughtUnits = jdbcTransactionDao.getBoughtUnitsPerDay(userID);
//        Map<LocalDate, Map<String, Double>> soldUnits = jdbcTransactionDao.getSoldUnitsPerDay(userID);
//
//
//        // TODO: optellen van waarden! (dus per datum alle units van verleden ook!)
//        // TODO: fixen als er op 1 datum geen transacties zijn -> TRY CATCH??? TO STREAM??
//
//        // start with portfolio of 0
//        // Iterate through all dates that units where bought
//        // Iterate through all crypto (symbols) that where bought that day
//
//
//        double portfolioValue = 0.0;
//        for (LocalDate date: boughtUnits.keySet()) {
//            for (String symbol : boughtUnits.get(date).keySet()) {
//                double positiveValue = courseValues.get(date).get(symbol) * boughtUnits.get(date).get(symbol);
//                portfolioValue += positiveValue;
//            }
//            totalPortfolioValue.put(date,portfolioValue);
//        }
//
//        // Iterate through all dates that units where sold
//        for (LocalDate date: soldUnits.keySet()) {
//            for (String symbol : soldUnits.get(date).keySet()) {
//                double NegativeValue = courseValues.get(date).get(symbol) * soldUnits.get(date).get(symbol);
//                portfolioValue -= NegativeValue;
//            }
//            if (totalPortfolioValue.containsKey(date)) {
//                totalPortfolioValue.put(date, ((totalPortfolioValue.get(date))-portfolioValue));
//            }
//            else {
//                totalPortfolioValue.put(date,portfolioValue);
//            }
//
//
//        }
//
//        // Iterate over each date since portfolio has units, starting with today
//
////            for (int days = 0; days <= portfolioDays; days++) {
////                double portfolioValue = 0.0;
////                LocalDate dayTeller = LocalDate.now().minusDays(days);
////                // Iterate over each crypto that portfolio has units of and add to total portfolio value
////                for (String symbol : boughtUnits.get(dayTeller).keySet()) {
////                    double cryptoValue = courseValues.get(dayTeller).get(symbol) * boughtUnits.get(dayTeller).get(symbol);
////                    portfolioValue += cryptoValue;
////                }
////                // inbouwen: als op die datum niets gekocht/verkocht is = error voorkomen
////                for (String symbol : soldUnits.get(dayTeller).keySet()) {
////                    double cryptoValue = courseValues.get(dayTeller).get(symbol) * soldUnits.get(dayTeller).get(symbol);
////                    portfolioValue -= cryptoValue;
////                }
////                totalPortfolioValue.put(dayTeller,portfolioValue);
////            }
//        return totalPortfolioValue;
//    }

