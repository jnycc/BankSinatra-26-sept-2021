package com.miw.controller;

import com.miw.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionController(){
        super();
        logger.info("New TransactionController-object created");
    }

   /* @PostMapping("/buy")
    public ResponseEntity<?> doTransaction(@RequestBody String something){

    }*/

    /*
    * TODO: methodes om Transactie te kunnen voltooien
    *
    * Transactie =
    * 1. Ingelogde Client klikt op Crypto die hij wil kopen & geeft aan hoeveel ervan hij wilt hebben (Post request?)
    * 2. Checks:
    *       a) Heeft seller het aantal crypto dat buyer wil kopen (e.g. buyer kan niet 5 BC kopen van seller als seller maar 2 BC heeft)?
    *       b) Heeft buyer genoeg saldo op zijn rekening om de transactie te voltooien? (= prijs van crypto + bankkosten!)
    *       c) Indien buyer & seller Clients zijn: heeft seller genoeg geld op zijn rekening om 50% van de bankkosten te betalen?
    * 3. Het aantal crypto wordt uit het portfolio van seller gehaald en in het portfolio van buyer gestopt
    * 4. De prijs van de crypto wordt uit het Account van buyer gehaald en in het Account van seller gestopt
    * 5. Bankkosten verrekenen:
    *       a) Buyer & seller zijn allebei Client --> betalen elk 50% van de bankkosten. Wordt uit hun Account gehaald en in het Account van bank gestopt
    *       b) Buyer is Client en seller is Bank --> buyer betaalt 100% van de bankkosten. Wordt uit zijn Account gehaald en in het Account van bank gestopt
    *       c) Buyer is Bank en seller is Client --> seller betaalt 100% van de bankkosten. Wordt uit zijn Account gehaald en in het Account van bank gestopt
    * 6. Transactie afgerond --> transactie wordt opgeslagen in de database
    * */


}
