package com.miw;

import com.miw.model.Bank;
import com.miw.model.Crypto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

// test
@SpringBootApplication
public class BankSinatraApplication {

    private static final int NR_CRYPTOS = 20;
    private static Bank bank;
    private static Set<Crypto> cryptos = new HashSet<>();

    public static void main(String[] args) {
        SpringApplication.run(BankSinatraApplication.class, args);

        // TODO Vul Bank portfolio met daadwerkelijke Cryptomunten
        for (int i = 1; i < NR_CRYPTOS + 1; i++) {
            Crypto crypto = new Crypto(String.format("%d", i), String.format("Crypto key %d", i), "Crypto description");
            cryptos.add(crypto);
        }
        bank = new Bank(cryptos);

        System.out.println(bank);
    }

}
