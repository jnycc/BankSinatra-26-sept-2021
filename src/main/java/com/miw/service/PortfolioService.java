package com.miw.service;

import com.miw.database.RootRepository;
import com.miw.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.TreeMap;


public class PortfolioService {

    private RootRepository rootRepository;
    private final Logger logger = LoggerFactory.getLogger(PortfolioService.class);

    @Autowired
    public PortfolioService(RootRepository rootRepository) {
        this.rootRepository = rootRepository;
    }

    // DAO aanroepen om portfolio uit db te halen
    // Bereken waarde per crypto (koers * eenheden)
    // Bereken totale waarde crypto
    // Bereken waardeveranderingen per crypto (dag - uit db, maand, 3 maanden, jaar)
    // --> 1/3/12 maanden live uit API halen?
    // Berekening waardeverandering

    //Aan frontend geven: Map met daarin elke crypto (key) en berekende cryptowaarden (value: Asset-object met daarin de berekende waarden)
    //Note: elke iban = alleen 1 portfolio met daarin meerdere assets. Daarom nu direct getAssets i.p.v. eerst getPortfolio als tussenstap.
    public Map<String, Asset> getAssets(int accountId) {
        Map<String, Asset> assets = new TreeMap<>();
        assets = rootRepository.getAssets(accountId);

        return assets;
    }

}