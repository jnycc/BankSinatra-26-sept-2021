package com.miw.service;

import com.miw.database.JdbcCryptoDao;
import com.miw.database.RootRepository;
import com.miw.model.Asset;
import com.miw.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PortfolioService {

    private RootRepository rootRepository;
    private JdbcCryptoDao jdbcCryptoDao;
    private final Logger logger = LoggerFactory.getLogger(PortfolioService.class);

    @Autowired
    public PortfolioService(RootRepository rootRepository, JdbcCryptoDao jdbcCryptoDao) {
        this.rootRepository = rootRepository;
        this.jdbcCryptoDao = jdbcCryptoDao;
    }

    public Client findClientByEmail (String email) {
        return rootRepository.findClientByEmail(email);
    }

    // DAO aanroepen om portfolio uit db te halen
    // Bereken waarde per crypto (koers * eenheden)
    // Bereken totale waarde crypto
    // Bereken waardeveranderingen per crypto (dag - uit db, maand, 3 maanden, jaar)
    // --> 1/3/12 maanden live uit API halen?

    //Aan frontend geven: Map met daarin elke crypto (key) en berekende cryptowaarden (value: Asset-object met daarin de berekende waarden)
    //Note: elke iban = alleen 1 portfolio met daarin meerdere assets. Daarom nu direct getAssets i.p.v. eerst getPortfolio als tussenstap.
    public Map<String, Asset> getAssets(int accountId) {
//        Map<String, Asset> assets = rootRepository.getAssets(accountId);
        Map<String, Asset> assets = new TreeMap<>();
        List<Asset> assetList = rootRepository.getAssets(accountId);
        for (Asset asset : assetList) {
            asset.setCurrentValue(calculateCurrentValue(asset));
            asset.setDeltaDayValue(calculateDeltaDayValue(asset, accountId));

            assets.put(asset.getCrypto().getName(), asset);
        }
        return assets;
    }

    private double calculateCurrentValue(Asset asset) {
        return asset.getUnits() * asset.getCrypto().getCryptoPrice();
    }

    public double calculateDeltaDayValue(Asset asset, int accountId) {
        //TODO: callt nu sql query voor ieder crypto in portfolio, nog efficiënter maken in 1 query.
        //TODO: testen of de pastPrice wel de juiste pakt
        double price1DayBefore = jdbcCryptoDao.getPastPriceBySymbol(asset.getCrypto().getSymbol(), 24);
        double unitsPurchasedAndSold = rootRepository.getSumOfUnitsPurchasedAndSold(accountId);
        double units1DayBefore = asset.getUnits() - unitsPurchasedAndSold;//lijst aan transacties ophalen, van current naar beginsaldo terugrekenen.
        double value1DayBefore = units1DayBefore * price1DayBefore;
        return asset.getCurrentValue() - value1DayBefore;
    }

}
