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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

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
                "(date, units, cryptoPrice, bankingFee, accountID_buyer, accountID_seller, symbol) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        //TODO: Hoera, wat een draak van een statement. Slaat op dit moment alleen datum op, niet tijd. LocalDateTime omzetten naar SQL.Date is een hel, blijkbaar. Andere oplossing voor vinden?
        ps.setDate(1, new java.sql.Date(transaction.getTransactionDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        ps.setDouble(2, transaction.getUnits());
        ps.setDouble(3, transaction.getCrypto().getCryptoPrice());
        ps.setDouble(4, transaction.getBankCosts());
        ps.setInt(5, transaction.getBuyer());
        ps.setInt(6, transaction.getSeller());
        ps.setString(7, transaction.getCrypto().getSymbol());
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

    public double getBankCosts(){
        String sql = "SELECT * FROM `Bankingfee`";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public double getSumOfUnitsPurchasedAndSold(int accountId, LocalDateTime dateTime, String symbol) {
        String sql = "SELECT " +
                "(SELECT SUM(units) FROM `Transaction` " +
                "WHERE accountID_buyer = ? AND date BETWEEN ? AND current_timestamp() AND symbol = ?) " +
                "-" +
                "(SELECT SUM(units) " +
                "FROM `Transaction` " +
                "WHERE accountID_seller = ? AND date BETWEEN ? AND current_timestamp() AND symbol = ?)" +
                "AS sumOfUnitsPurchasedAndSold;";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, accountId, dateTime, symbol, accountId, dateTime, symbol);
        } catch (NullPointerException e) {
            return 0;
        }
    }

//    public Map<String, Double> getSumOfUnitsPurchasedAndSold(int accountId, LocalDateTime dateTime, String symbol) {
//        String sql = "SELECT " +
//                "(SELECT SUM(units) FROM `Transaction` " +
//                "WHERE accountID_buyer = ? AND date BETWEEN ? AND current_timestamp()" +
//                "AND cryptoID = (SELECT cryptoID FROM Crypto WHERE symbol = ?)) " +
//                "-" +
//                "(SELECT SUM(units) " +
//                "FROM `Transaction` " +
//                "WHERE accountID_seller = ? AND date BETWEEN ? AND current_timestamp() " +
//                "AND cryptoID = (SELECT cryptoID FROM Crypto WHERE symbol = ?))" +
//                "AS sumOfUnitsPurchasedAndSold;";
//        try {
//            return jdbcTemplate.queryForObject(sql, Double.class, accountId, dateTime, symbol, accountId, dateTime, symbol);
//        } catch (NullPointerException e) {
//            return 0;
//        }
//    }
}