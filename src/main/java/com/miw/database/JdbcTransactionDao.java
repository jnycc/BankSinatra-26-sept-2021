package com.miw.database;

import com.miw.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

@Repository
public class JdbcTransactionDao {

    private final Logger logger = LoggerFactory.getLogger(JdbcTransactionDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcTransactionDao");
    }

    private PreparedStatement insertTransactionStatement(Transaction transaction, Connection connection) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("INSERT INTO Transaction " +
                "(date, units, exchangeRate, bankingFee, accountID_buyer, accountID_seller, cryptoID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setDate(1, java.sql.Date.valueOf(transaction.getTransactionDate().toLocalDate())); //TODO: dit moet anders kunnen denk ik :)
        ps.setDouble(2, transaction.getUnits());
        ps.setDouble(3, transaction.getCrypto().getCryptoPrice());
        ps.setDouble(4, transaction.getBankCosts());
        ps.setInt(5, transaction.getBuyer().getUserId());
        ps.setInt(6, transaction.getSeller().getUserId());
        ps.setInt(7, transaction.getCrypto().getCryptoId()); //TODO: manier om CryptoID te setten! Of we moeten dit anders doen
        return ps;
    }

    public Transaction save(Transaction transaction){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertTransactionStatement(transaction, connection), keyHolder);
        int transactionId = keyHolder.getKey().intValue();
        transaction.setTransactionId(transactionId);
        logger.info("New transaction has been saved to the database");
        return transaction;
    }

}