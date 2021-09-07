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
                "INSERT INTO CryptoPrice (cryptoID, cryptoPrice, dateRetrieved) " +
                        "VALUES ((SELECT cryptoID FROM Crypto WHERE symbol = ?), ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, symbol);
        ps.setDouble(2, price);
        ps.setTimestamp(3, Timestamp.valueOf(time));
        return ps;
    }

    // selecteert crypto en neemt automatisch de meest recente opgeslagen prijs
    public Crypto getCryptoBySymbol(String symbol) {
        String sql = "SELECT Crypto.*, CryptoPrice.cryptoPrice " +
                "FROM Crypto LEFT JOIN CryptoPrice " +
                "ON Crypto.cryptoID = CryptoPrice.cryptoID " +
                "WHERE Crypto.cryptoID = " +
                "    (SELECT cryptoID " +
                "    FROM Crypto " +
                "    WHERE symbol = ?) " +
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

    // vraagt de koers op die het dichtst bij het tijdstip [nu minus X aantal uur in het verleden] is opgeslagen
    public double getPastPriceBySymbol(String symbol, int hoursAgo) {
        String sql = "SELECT cryptoPrice FROM CryptoPrice WHERE cryptoID = (SELECT cryptoID FROM Crypto WHERE symbol = ?) " +
                "ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, (NOW() - INTERVAL ? HOUR))) LIMIT 1;" ;
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, symbol, hoursAgo);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Failed to get past crypto price by symbol");
            return 0;
        }
    }

    public double getPriceOnDateTimeBySymbol(String symbol, LocalDateTime dateTime) {
        String sql = "SELECT cryptoPrice FROM CryptoPrice WHERE cryptoID = (SELECT cryptoID FROM Crypto WHERE symbol = ?) " +
                "ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, ?)) LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, symbol, dateTime);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Failed to get selected crypto price by symbol on the selected dateTime.");
            return 0;
        }
    }

    public void saveCryptoPriceBySymbol(String symbol, double price, LocalDateTime time) {
        String symbolClean = symbol.substring(1, (symbol.length() - 1)); //clean up quotation marks leftover from json
        jdbcTemplate.update(connection -> insertPriceStatement(symbolClean, price, time, connection));
    }

    private static class CryptoRowMapper implements RowMapper<Crypto> {

        @Override
        public Crypto mapRow(ResultSet resultSet, int i) throws SQLException {
            int cryptoID  = resultSet.getInt("cryptoID");
            String name  = resultSet.getString("name");
            String symbol = resultSet.getString("symbol");
            String description = resultSet.getString("description");
            Double price = resultSet.getDouble("cryptoPrice");
            Crypto crypto = new Crypto(name, symbol, description, price);
            crypto.setCryptoId(cryptoID);
            return crypto;
        }
    }
}
