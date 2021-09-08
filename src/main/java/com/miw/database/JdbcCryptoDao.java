package com.miw.database;

import com.miw.model.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;

// TODO: id's uit crypto wegwerken, primary key -> symbol

@Repository
public class JdbcCryptoDao {

    private final Logger logger = LoggerFactory.getLogger(JdbcCryptoDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCryptoDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcCryptoDao");
    }

    // insert met gebruik van symbol ipv id
    private PreparedStatement insertPriceStatement
    (String symbol, double price, LocalDateTime time, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO CryptoPrice (symbol, cryptoPrice, dateRetrieved) " +
                        "VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, symbol);
        ps.setDouble(2, price);
        ps.setTimestamp(3, Timestamp.valueOf(time));
        return ps;
    }

    // Selecteert een crypto uit de db, neemt daarbij automatisch de meest recente opgeslagen prijs in het object op
    public Crypto getCryptoBySymbol(String symbol) {
        String sql = "SELECT Crypto.*, CryptoPrice.cryptoPrice " +
                "FROM Crypto LEFT JOIN CryptoPrice " +
                "ON Crypto.symbol = CryptoPrice.symbol " +
                "WHERE Crypto.symbol = ? " +
                "AND CryptoPrice.dateRetrieved = " +
                "    (SELECT MAX(dateRetrieved) " +
                "    FROM CryptoPrice);";
        try {
            return jdbcTemplate.queryForObject(sql, new JdbcCryptoDao.CryptoRowMapper(), symbol);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Failed to get crypto by symbol");
            return null;
        }
    }

    // Haalt enkel de laatste koers van een gegeven crypto op
    public double getLatestPriceBySymbol(String symbol) {
        return getCryptoBySymbol(symbol).getCryptoPrice();
    }

    // Vraagt de koers op die het dichtst bij het tijdstip [nu minus X aantal uur in het verleden] is opgeslagen
    public double getPastPriceBySymbol(String symbol, int hoursAgo) {
        String sql = "SELECT cryptoPrice FROM CryptoPrice WHERE symbol = ? " +
                "ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, (NOW() - INTERVAL ? HOUR))) LIMIT 1;" ;
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, symbol, hoursAgo);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Failed to get past crypto price by symbol");
            return 0;
        }
    }

    public double getPriceOnDateTimeBySymbol(String symbol, LocalDateTime dateTime) {
        String sql = "SELECT cryptoPrice FROM CryptoPrice WHERE symbol = ? " +
                "ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, ?)) LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, symbol, dateTime);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Failed to get selected crypto price by symbol on the selected dateTime.");
            return 0;
        }
    }

    public void saveCryptoPriceBySymbol(String symbol, double price, LocalDateTime time) {
        jdbcTemplate.update(connection -> insertPriceStatement(symbol, price, time, connection));
    }

    private static class CryptoRowMapper implements RowMapper<Crypto> {

        @Override
        public Crypto mapRow(ResultSet resultSet, int i) throws SQLException {
            String name  = resultSet.getString("name");
            String symbol = resultSet.getString("symbol");
            String description = resultSet.getString("description");
            Double price = resultSet.getDouble("cryptoPrice");
            Crypto crypto = new Crypto(name, symbol, description, price);
            return crypto;
        }
    }
}
