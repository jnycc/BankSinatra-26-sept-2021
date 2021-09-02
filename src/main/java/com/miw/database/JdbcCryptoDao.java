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
    // selecteert crypto en neemt automatisch de meest recente opgeslagen prijs
    public Crypto getCryptoBySymbol(String symbol) {
        String sql = "SELECT Crypto.*, CryptoPrice.cryptoPrice " +
                "FROM Crypto LEFT JOIN CryptoPrice " +
                "ON Crypto.cryptoID = CryptoPrice.cryptoID " +
                "WHERE Crypto.cryptoID = " +
                "   (SELECT cryptoID " +
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
