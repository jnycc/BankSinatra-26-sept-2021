package com.miw.service;

import com.miw.database.JdbcAssetDao;
import com.miw.database.JdbcCryptoDao;
import com.miw.database.JdbcTransactionDao;
import com.miw.model.Asset;
import com.miw.model.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public ArrayList<Double> getCryptoStats(int daysBack, String cryptoSymbol) {
        ArrayList<Double> cryptoStats = new ArrayList<>();
        for (int i = daysBack; i < 0; i--) {
            // juiste datum (x) ophalen (currentDate - i)
            LocalDate date = (LocalDate.now().minusDays(i));
            // getTransactionsAsBuyer op basis van betrefende datum
//            jdbcTransactionDao.getAssetsUntill(date);
            // getTransactionsAsSeller op basis van betrefende datum
//            jdbcTransactionDao.getTransactionsSellerUntillDate(date);
            // double totaalPortfolioWaarde;
            // foreach loop
                    // ga transactie na, per transactie
                    // haal koerswaarde op van cryptosymbol op datum x. vermenigvuldig aandeel keer koerswaarde
                    // totaalportfoliowaarde =+ deze koerswaarde
            // cryptoStats.add totaalportfoliowaarde
        }
        return cryptoStats;
    }
    //public void

    // TODO: Method to get list of total portfolio value with certain interval (base case: each day of the last week):


}
