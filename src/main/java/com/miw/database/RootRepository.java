/**
 * @Author: Johnny Chan, MIW student 500878034.
 * Deze class is de Repository welke diverse DAO's aanroept om
 * informatie uit de database te verkrijgen en te combineren.
 */
package com.miw.database;

import com.miw.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RootRepository {

    private final Logger logger = LoggerFactory.getLogger(RootRepository.class);

    private ClientDao clientDAO;
    private JdbcAccountDao jdbcAccountDao; // TODO: interface aanroepen ipv jdbcAccountDAO zelf
    private JdbcAdminDao jdbcAdminDao; // TODO: interface aanroepen ipv jdbcAdminDao zelf
    private JdbcAssetDao jdbcAssetDao;
    private JdbcTransactionDao jdbcTransactionDao; //TODO: interface aanroepen ipv JdbcTransactionDao zelf

    @Autowired
    public RootRepository(ClientDao clientDAO, JdbcAccountDao jdbcAccountDao, JdbcAdminDao jdbcAdminDao, JdbcAssetDao jdbcAssetDao, JdbcTransactionDao jdbcTransactionDao) { // TODO: interface aanroepen ipv jdbcAccountDao, jdbcAdminDao en jdbcTransactionDao zelf
        super();
        this.clientDAO = clientDAO;
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcAdminDao = jdbcAdminDao;
        this.jdbcAssetDao = jdbcAssetDao;
        this.jdbcTransactionDao = jdbcTransactionDao;
        logger.info("New RootRepository-object created");
    }

    public Client saveNewClient(Client client) {
        client = clientDAO.save(client); //client in table User opslaan
        //client's account in table Account opslaan en PK accountId verkrijgen en opslaan in account-object
        Account updatedAccount = jdbcAccountDao.save(client.getAccount(), client.getUserId());
        client.getAccount().setAccountId(updatedAccount.getAccountId());
        return client;
    }

    public Administrator saveNewAdmin(Administrator admin) {
        jdbcAdminDao.save(admin);
        return admin;
    }

    public Transaction saveNewTransaction(Transaction transaction){
        jdbcTransactionDao.save(transaction);
        return transaction;
    }

    public Client findClientByEmail(String email) {
        Client client = clientDAO.findByEmail(email);
        if (client != null) {
            client.setAccount(getAccountByEmail(email));
        }
        return client;
    }

    public Administrator findAdminByEmail(String email) {
        return jdbcAdminDao.findByEmail(email);
    }

    public Account getAccountById(int accountId){
        return jdbcAccountDao.getAccountById(accountId);
    }

    public Account getAccountByEmail(String email){
        return jdbcAccountDao.getAccountByEmail(email);
    }

    public void updateBalance(double newBalance, int accountId){
        jdbcAccountDao.updateBalance(newBalance, accountId);
    }

    public List<Asset> getAssets(int accountId) {
        List<Asset> assets = jdbcAssetDao.getAssets(accountId);
        return assets;
    }

    public Asset getAssetBySymbol(int accountId, String symbol){
        return jdbcAssetDao.getAssetBySymbol(accountId, symbol);
    }

    public double getSumOfUnitsPurchasedAndSold(int accountId, LocalDateTime dateTime, String symbol) {
        return jdbcTransactionDao.getSumOfUnitsPurchasedAndSold(accountId, dateTime, symbol);
    }

    public void saveAccount(Account account, int userId) {
        jdbcAccountDao.save(account, userId);
    }
}
