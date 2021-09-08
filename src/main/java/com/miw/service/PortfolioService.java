package com.miw.service;

import com.miw.database.JdbcCryptoDao;
import com.miw.database.RootRepository;
import com.miw.model.Asset;
import com.miw.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PortfolioService {

    private RootRepository rootRepository;
    private JdbcCryptoDao jdbcCryptoDao;
    private final Logger logger = LoggerFactory.getLogger(PortfolioService.class);
    private static DecimalFormat df = new DecimalFormat("#.##");
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
    public Map<String, Object> getPortfolio(int accountId) {
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
    }

    private double calculateCurrentValue(Asset asset) {
        return asset.getUnits() * asset.getCrypto().getCryptoPrice();
    }

    private Asset calculatePortfolioHistoricalValue() {
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
        return null;
    }

    private void setDeltaValues(int accountId, Asset asset) {
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
    }

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
     * @param accountId SQL PK of the portfolio account
     * @param asset the crypto currency
     * @param dateTime point in time of which the crypto value is needed
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
