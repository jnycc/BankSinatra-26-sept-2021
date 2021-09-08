/**
 * @Author Johnny Chan
 * @Description This class obtains and calculates the client's portfolio with all assets ever owned,
 * including the current, historical and delta values. The portfolio is returned to the PortfolioController.
 */
package com.miw.service;

import com.miw.database.JdbcCryptoDao;
import com.miw.database.RootRepository;
import com.miw.model.Asset;
import com.miw.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PortfolioService {

    private final RootRepository rootRepository;
    private final JdbcCryptoDao jdbcCryptoDao;
    private Map<String, Asset> portfolioAssets = new TreeMap<>();

    @Autowired
    public PortfolioService(RootRepository rootRepository, JdbcCryptoDao jdbcCryptoDao) {
        this.rootRepository = rootRepository;
        this.jdbcCryptoDao = jdbcCryptoDao;
    }

    public Client findClientByEmail(String email) {
        return rootRepository.findClientByEmail(email);
    }

    /**
     * Obtains the client's portfolio with all assets, including the current, historical and delta values.
     * Delta values are changes in the value of the assets and total portfolio
     * compared to 1 day/1 month/3 months/1 year ago/start date.
     * @param accountId client's accountId as in the database
     * @return Client's portfolio with all assets, including the current, historical and delta values.
     */
    // Door map van alle portfolioAssets loopen (global attribute) en hiervan deltawaarden berekenen + totale historische waarden
    public Map<String, Object> getPortfolio(int accountId) {
        calculatePortfolioHistoricalValue(accountId); //vult een global map met Assets. Ieder asset bevat de current, historical en deltawaarden in eigen maps.
        //loopen door de portfolio-map met assets, hiervan de totale deltawaarde van de gehele portfolio berekenen.
        double totalCurrentValue = 0.0;
        double totalValue1DayAgo = 0.0;
        double totalValue1MonthAgo = 0.0;
        for (Asset asset : portfolioAssets.values()) {
            totalCurrentValue += asset.getCurrentValue();
            totalValue1DayAgo += asset.getHistoricalValues().get("value1DayAgo");
            totalValue1MonthAgo += asset.getHistoricalValues().get("value1MonthAgo");
        }
        Map<String, Object> portfolio = new TreeMap<>();
        portfolio.put("Portfolio assets", portfolioAssets);
        portfolio.put("Total current value", totalCurrentValue);
        portfolio.put("Total delta 1 day value", totalCurrentValue - totalValue1DayAgo);
        portfolio.put("Total delta 1 day %", (totalCurrentValue - totalValue1DayAgo) / totalValue1DayAgo);
        portfolio.put("Total delta 1 month value", totalCurrentValue - totalValue1MonthAgo);
        portfolio.put("Total delta 1 month %", (totalCurrentValue - totalValue1MonthAgo) / totalValue1MonthAgo);
        return portfolio;
    }

    //Haal map van assets op dateTime x op o.b.v. Transaction-history met columns: symbol, sumOfTransactions
    // loop erdoorheen en voor elke asset:
    // -> terugrekenen naar historicalUnits en opslaan in map in Asset
    // -> historicalPrice ophalen
    // -> historical value (p*q) berekenen, deze opslaan in een aparte Map
    // -> voor elke huidige asset de delta value van de asset berekenen, opslaan in map in Asset

    //Result: 2 objecten
    // a. map met Assets (met daarin map met historical units en deltawaarden)
    // b. map met total historical deltawaarden van gehele portfolio
    /**
     * Obtains a list of all crypto-assets ever owned by the user from the database.
     * For each crypto-asset ever owned, calculates the difference between the current value (units * current price)
     * and the value at a given historical dateTime (units on dateTime * price on dateTime).
     * This reflects both changes in market price and volume (units of assets purchased/sold).
     * @param accountId client's accountId as in the database
     */
    private void calculatePortfolioHistoricalValue(int accountId) {
        List<String> allCryptosOwned = rootRepository.getAllCryptosOwned(accountId);
        Asset asset;
        for (String symbol : allCryptosOwned) {
            asset = rootRepository.getAssetBySymbol(accountId, symbol);

            Double units1DayAgo = rootRepository.getSymbolUnitsAtDateTime(accountId, symbol, LocalDateTime.now().minusDays(1));
            asset.getHistoricalNrOfUnits().put("units1DayAgo", units1DayAgo);
            Double price1DayAgo = jdbcCryptoDao.getPriceOnDateTimeBySymbol(symbol, LocalDateTime.now().minusDays(1));
            Double value1DayAgo = price1DayAgo * units1DayAgo;
            asset.getHistoricalValues().put("value1DayAgo", value1DayAgo);

            asset.getDeltaValues().put("delta1DayValue", asset.getCurrentValue() - value1DayAgo);
            asset.getDeltaValues().put("delta1DayPct", calculateDeltaPct(asset.getCurrentValue(), value1DayAgo));

            Double units1MonthAgo = rootRepository.getSymbolUnitsAtDateTime(accountId, symbol, LocalDateTime.now().minusMonths(1));
            asset.getHistoricalNrOfUnits().put("units1MonthAgo", units1MonthAgo);
            Double price1MonthAgo = jdbcCryptoDao.getPriceOnDateTimeBySymbol(symbol, LocalDateTime.now().minusMonths(1));
            Double value1MonthAgo = price1MonthAgo * units1MonthAgo;
            asset.getHistoricalValues().put("value1MonthAgo", value1MonthAgo);

            asset.getDeltaValues().put("delta1MonthValue", asset.getCurrentValue() - value1MonthAgo);
            asset.getDeltaValues().put("delta1MonthPct", calculateDeltaPct(asset.getCurrentValue(), value1MonthAgo));

            portfolioAssets.put(symbol, asset); //asset met current, historical en deltawaarden opslaan in global map
        }
    }

    private double calculateDeltaPct(double currentValue, double historicalValue) {
        double deltaValuePct = ((currentValue - historicalValue) / historicalValue) * 100;
        if (Double.isInfinite(deltaValuePct)) {
            return 100;
        } else {
            return deltaValuePct;
        }
    }
}
