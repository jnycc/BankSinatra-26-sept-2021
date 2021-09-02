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

@Repository
public class RootRepository {

    private final Logger logger = LoggerFactory.getLogger(RootRepository.class);

    private ClientDao clientDAO;
    private JdbcAccountDao jdbcAccountDao; // TODO: interface aanroepen ipv jdbcAccountDAO zelf
    private JdbcAdminDao jdbcAdminDao; // TODO: interface aanroepen ipv jdbcAdminDao zelf
    private JdbcTransactionDao jdbcTransactionDao; //TODO: interface aanroepen ipv JdbcTransactionDao zelf

    @Autowired
    public RootRepository(ClientDao clientDAO, JdbcAccountDao jdbcAccountDao, JdbcAdminDao jdbcAdminDao, JdbcTransactionDao jdbcTransactionDao) { // TODO: interface aanroepen ipv jdbcAccountDao, jdbcAdminDao en jdbcTransactionDao zelf
        super();
        this.clientDAO = clientDAO;
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcAdminDao = jdbcAdminDao;
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

    public User findClientByEmail(String email) {
        return clientDAO.findByEmail(email);
    }

    public Administrator findAdminByEmail(String email) {
        return jdbcAdminDao.findByEmail(email);
    }

    public Account getAccountByEmail(String email){
        return jdbcAccountDao.getAccountByEmail(email);
    }

    public void updateBalance(double newBalance, int accountId){
        jdbcAccountDao.updateBalance(newBalance, accountId);
    }
}
