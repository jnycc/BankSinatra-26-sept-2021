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
import java.time.ZoneId;

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
        //TODO: Hoera, wat een draak van een statement. Slaat op dit moment alleen datum op, niet tijd. LocalDateTime omzetten naar SQL.Date is een hel, blijkbaar. Andere oplossing voor vinden?
        ps.setDate(1, new java.sql.Date(transaction.getTransactionDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        ps.setDouble(2, transaction.getUnits());
        ps.setDouble(3, transaction.getCrypto().getCryptoPrice());
        ps.setDouble(4, transaction.getBankCosts());
        ps.setInt(5, transaction.getBuyer());
        ps.setInt(6, transaction.getSeller());
        ps.setInt(7, transaction.getCrypto().getCryptoId());
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

    //TODO: aanpassen zodat ie een parameter kan gebruiken voor verschillende situaties: day, month, 3 months, year, start
    public double getSumOfUnitsPurchasedAndSold(int accountId) {
        String sql = "SELECT " +
                "(SELECT SUM(units) FROM `Transaction` " +
                "WHERE accountID_buyer = ? AND date BETWEEN current_date() AND current_timestamp()) " +
                "-" +
                "(SELECT SUM(units) " +
                "FROM `Transaction` " +
                "WHERE accountID_seller = ? AND date BETWEEN current_date() AND current_timestamp()) " +
                "AS sumOfUnitsPurchasedAndSold;";
        return jdbcTemplate.queryForObject(sql, Double.class, accountId, accountId);
    }
}