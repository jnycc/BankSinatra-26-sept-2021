package com.miw;

import com.miw.database.RootRepository;
import com.miw.model.Bank;
import com.miw.model.Crypto;
import com.miw.service.CryptoPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.Set;
import java.util.TreeSet;

@EnableScheduling
@SpringBootApplication
public class BankSinatraApplication {

    private Set<Crypto> cryptos = new TreeSet<>();
    private Logger logger = LoggerFactory.getLogger(BankSinatraApplication.class);
    static final int BANK_ID = 1;
    // TODO: Pas zo nodig TOP 20 Cryptos aan
    private final String[] TOP20_CRYPTO = {"BTC", "ETH", "ADA", "BNB", "USDT", "XRP", "DOGE", "SOL", "DOT",
                        "USDC", "UNI", "LINK", "LUNA", "BCH", "BUSD", "LTC", "ICP", "WBTC", "MATIC", "VET"};

    @Autowired
    public BankSinatraApplication(CryptoPriceService cryptoPriceService, RootRepository repository) {
        for (String s : TOP20_CRYPTO) {
            cryptos.add(cryptoPriceService.getJdbcCryptoDao().getCryptoBySymbol(s));
        }
        Bank.getBankSinatra().setUpInitialPortfolio(cryptos);
        repository.saveAccount(Bank.getBankSinatra().getAccount(), BANK_ID);
        logger.info("Bank Sinatra is founded");
    }

    public static void main(String[] args) {
        SpringApplication.run(BankSinatraApplication.class, args);
    }
}
