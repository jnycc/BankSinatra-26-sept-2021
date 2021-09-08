package com.miw.database;

import com.miw.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public double getSumOfUnitsPurchasedAndSold(int accountId, LocalDateTime dateTime, String symbol) {
        String sql = "SELECT " +
                "(SELECT IFNULL(SUM(units), 0) FROM `Transaction` " +
                "WHERE accountID_buyer = ? AND date BETWEEN ? AND current_timestamp() AND symbol = ?) " +
                "-" +
                "(SELECT IFNULL(SUM(units), 0) FROM `Transaction` " +
                "WHERE accountID_seller = ? AND date BETWEEN ? AND current_timestamp() AND symbol = ?)" +
                "AS sumOfUnitsPurchasedAndSold;";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, accountId, dateTime, symbol, accountId, dateTime, symbol);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public List<String> getAllCryptosOwned(int accountId) {
        String sql = "(SELECT symbol FROM `Transaction` WHERE accountID_buyer = ? OR accountID_seller = ? " +
                "GROUP BY symbol) UNION (SELECT symbol FROM Asset WHERE accountID = ?);";
        return jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("symbol");
            }
        }, accountId, accountId, accountId);
    }

//    public Map<String, Double> getSumOfAllTransactions(int accountId, LocalDateTime dateTime) {
//        String sql = "SELECT symbol, " +
//                "(SELECT SUM(units) FROM `Transaction` " +
//                "WHERE accountID_buyer = ? AND date BETWEEN ? AND current_timestamp()) " +
//                "-" +
//                "(SELECT SUM(units) " +
//                "FROM `Transaction` " +
//                "WHERE accountID_seller = ? AND date BETWEEN ? AND current_timestamp())" +
//                "AS sumOfUnitsPurchasedAndSold " +
//                "FROM `Transaction` GROUP BY symbol;";
//        try {
//            return jdbcTemplate.query(sql, new TransactionResultSetExtractor(), accountId, dateTime, accountId, dateTime);
//        } catch (NullPointerException e) {
//            return null;
//        }
//    }
//
//
//    private static class TransactionResultSetExtractor implements ResultSetExtractor<Map<String, Double>> {
//
//        @Override
//        public Map<String, Double> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//            Map<String, Double> cryptosPurchasedAndSold = new TreeMap<>();
//            while(resultSet.next()) {
//                String symbol = resultSet.getString("symbol");
//                double sumOfUnitsPurchasedAndSold = resultSet.getDouble("sumOfUnitsPurchasedAndSold");
//                cryptosPurchasedAndSold.put(symbol, sumOfUnitsPurchasedAndSold);
//            }
//            return cryptosPurchasedAndSold;
//        }
//    }
}