package com.miw.service;

import com.miw.database.JdbcCryptoDao;
import com.miw.database.RootRepository;
import com.miw.model.Asset;
import com.miw.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PortfolioService {

    private final RootRepository rootRepository;
    private final JdbcCryptoDao jdbcCryptoDao;
    private final static DecimalFormat df = new DecimalFormat("#.##");
    private Map<String, Asset> portfolioAssets = new TreeMap<>();

    @Autowired
    public PortfolioService(RootRepository rootRepository, JdbcCryptoDao jdbcCryptoDao) {
        this.rootRepository = rootRepository;
        this.jdbcCryptoDao = jdbcCryptoDao;
    }

    public Client findClientByEmail(String email) {
        return rootRepository.findClientByEmail(email);
    }

    // DAO aanroepen om portfolio uit db te halen
    // Bereken waarde per crypto (koers * eenheden)
    // Bereken totale waarde crypto
    // Bereken waardeveranderingen per crypto (dag - uit db, maand, 3 maanden, jaar)
    // --> 1/3/12 maanden live uit API halen?

    //Aan frontend geven: Map met daarin elke crypto (key) en berekende cryptowaarden (value: Asset-object met daarin de berekende waarden)
    //Note: elke iban = alleen 1 portfolio met daarin meerdere assets. Daarom nu direct getAssets i.p.v. eerst getPortfolio als tussenstap.
    /*public Map<String, Object> getPortfolio(int accountId) {
        List<Asset> assetList = rootRepository.getAssets(accountId);
        System.out.println(assetList);
        Map<String, Object> portfolio = new TreeMap<>();
        double totalCurrentValuePortfolio = 0.0;
        for (Asset asset : assetList) {
            asset.setCurrentValue(calculateCurrentValue(asset));
            totalCurrentValuePortfolio += asset.getCurrentValue();
            setDeltaValues(accountId, asset);
            portfolio.put(asset.getCrypto().getName(), asset);
        }
        portfolio.put("Total current value", totalCurrentValuePortfolio);
//        portfolio.put("Total historical values", )
        return portfolio;
    }*/

    private double calculateCurrentValue(Asset asset) {
        return asset.getUnits() * asset.getCrypto().getCryptoPrice();
    }

    public Map<String, Object> getPortfolio2(int accountId) {
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

    private void calculatePortfolioHistoricalValue(int accountId) {
        //Haal map van assets op dateTime x op o.b.v. Transaction-history met columns: symbol, sumOfTransactions
        // loop erdoorheen en voor elke asset:
        // -> terugrekenen naar historicalUnits en opslaan in map in Asset
        // -> historicalPrice ophalen
        // -> historical value (p*q) berekenen, deze opslaan in een aparte Map
        // -> voor elke huidige asset de delta value van de asset berekenen, opslaan in map in Asset
        //=> Asset returnen zodat ie daar in een map gestopt wordt.

        //Later door de aparte map met historical values loopen om de totale deltawaarde (van elke asset currentvalue - historicalvalue) te berekenen

        //2 objecten:
        // a. map met Assets (met daarin map met historical units en deltawaarden)
        // b. map met total historical deltawaarden van gehele portfolio
//        Map<String, Double> sumOfAllTransactions = rootRepository.getSumOfAllTransactions(accountId, )

        List<String> allCryptosOwned = rootRepository.getAllCryptosOwned(accountId);
        Asset asset;
        for (String symbol : allCryptosOwned) {
            asset = rootRepository.getAssetBySymbol(accountId, symbol);
            Double units1DayAgo = rootRepository.getSymbolUnitsAtDateTime(accountId, symbol, LocalDateTime.now().minusDays(1));
            asset.getHistoricalNrOfUnits().put("units1DayAgo", units1DayAgo);
            Double price1DayAgo = jdbcCryptoDao.getPriceOnDateTimeBySymbol(symbol, LocalDateTime.now().minusDays(1));
            Double value1DayAgo = price1DayAgo * units1DayAgo;
            asset.getHistoricalValues().put("value1DayAgo", value1DayAgo);

            Double delta1DayValue = asset.getCurrentValue() - value1DayAgo;
            Double delta1DayPct = delta1DayValue / value1DayAgo;
            asset.getDeltaValues().put("delta1DayValue", delta1DayValue);
            asset.getDeltaValues().put("delta1DayPct", delta1DayPct);

            Double units1MonthAgo = rootRepository.getSymbolUnitsAtDateTime(accountId, symbol, LocalDateTime.now().minusMonths(1));
            asset.getHistoricalNrOfUnits().put("units1MonthAgo", units1MonthAgo);
            Double price1MonthAgo = jdbcCryptoDao.getPriceOnDateTimeBySymbol(symbol, LocalDateTime.now().minusMonths(1));
            Double value1MonthAgo = price1MonthAgo * units1MonthAgo;
            asset.getHistoricalValues().put("value1MonthAgo", value1MonthAgo);

            Double delta1MonthValue = asset.getCurrentValue() - value1MonthAgo;
            Double delta1MonthPct = delta1MonthValue / value1MonthAgo;
            asset.getDeltaValues().put("delta1MonthValue", delta1MonthValue);
            asset.getDeltaValues().put("delta1MonthPct", delta1MonthPct);

            portfolioAssets.put(symbol, asset); //asset met current, historical en deltawaarden opslaan in global map
        }
    }

    // Door map van alle assets loopen (global attribute) en hiervan deltawaarden berekenen + totale historische waarden

    /*private void setDeltaValues(int accountId, Asset asset) {
        double currentValue = asset.getCurrentValue();
        double deltaValue = calculateDeltaValue(accountId, asset, LocalDateTime.now().minusDays(1));
        asset.setDelta1DayValue(deltaValue);
        asset.setDelta1DayPct(calculateDeltaPct(deltaValue, currentValue));

        deltaValue = calculateDeltaValue(accountId, asset, LocalDateTime.now().minusMonths(1));
        asset.setDelta1MonthValue(deltaValue);
        asset.setDelta1MonthPct(calculateDeltaPct(deltaValue, currentValue));

        deltaValue = calculateDeltaValue(accountId, asset, LocalDateTime.now().minusMonths(3));
        asset.setDelta3MonthsValue(deltaValue);
        asset.setDelta3MonthsPct(calculateDeltaPct(deltaValue, currentValue));

        deltaValue = calculateDeltaValue(accountId, asset, LocalDateTime.now().minusYears(1));
        asset.setDeltaYearValue(deltaValue);
        asset.setDeltaYearPct(calculateDeltaPct(deltaValue, currentValue));


//        deltaValue = //startdate = allereerste transactie
//        asset.setDeltaStartValue(deltaValue);
//        asset.setDeltaStartValuePct(deltaValue / (currentValue - deltaValue));
    }*/

    private double calculateDeltaPct(double deltaValue, double currentValue) {
        double deltaValuePct = (deltaValue / (currentValue - deltaValue)) * 100;
        if (Double.isInfinite(deltaValuePct)) {
            return 100;
        } else {
            return deltaValuePct;
        }
    }

    /**
     * Calculates the difference between the total current value and the total value at a given historical dateTime
     * of a specified crypto-asset. This reflects both changes in market price and volume (units of assets purchased/sold).
     *
     * @param accountId SQL PK of the portfolio account
     * @param asset     the crypto currency
     * @param dateTime  point in time of which the crypto value is needed
     * @return difference between the total current value and the total value at the specified point of time of the crypto asset.
     */
    public double calculateDeltaValue(int accountId, Asset asset, LocalDateTime dateTime) {
        double priceBefore = jdbcCryptoDao.getPriceOnDateTimeBySymbol(asset.getCrypto().getSymbol(), dateTime);
//        System.out.println(asset.getCrypto().getName());
//        System.out.println("price before: " + priceBefore);
        double unitsPurchasedAndSold = rootRepository.getSumOfUnitsPurchasedAndSold(accountId, dateTime, asset.getCrypto().getSymbol());
//        System.out.println("units delta: " + unitsPurchasedAndSold);
        double unitsBefore = (unitsPurchasedAndSold != 0) ? (asset.getUnits() - unitsPurchasedAndSold) : 0;//lijst aan transacties ophalen, van current naar beginsaldo terugrekenen.
//        System.out.println("units before: " + unitsBefore + "\nunits now: " + asset.getUnits());
        double valueBefore = unitsBefore * priceBefore;
//        System.out.println("value before: " + valueBefore + "\n");
        return asset.getCurrentValue() - valueBefore;
    }
}
