package com.miw.database;

import com.miw.model.Asset;
import com.miw.model.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class JdbcAssetDao {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(JdbcAssetDao.class);

    public JdbcAssetDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("JdbcAssetDAO-object created.");
    }

    public Map<String, Asset> getAssets(int accountId) {
        String sql = "SELECT accountID, a.cryptoID, `name`, symbol, cryptoPrice, `description`, units, `dateRetrieved` " +
                "FROM (Asset a JOIN Crypto c ON a.cryptoID = c.cryptoID) " +
                "JOIN CryptoPrice p ON p.cryptoID = c.cryptoID " +
                "WHERE accountID = ?;";
        return jdbcTemplate.query(sql, new AssetResultSetExtractor(), accountId);
    }


    private static class AssetResultSetExtractor implements ResultSetExtractor<Map<String, Asset>> {

        @Override
        public Map<String, Asset> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<String, Asset> assetMap = new TreeMap<>();
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                String symbol = resultSet.getString("symbol");
                String description = resultSet.getString("description");
                double cryptoPrice = resultSet.getDouble("cryptoPrice");
                Crypto crypto = new Crypto(name, symbol, description, cryptoPrice);
                double units = resultSet.getDouble("units");
                Asset asset = new Asset(crypto, units);
                assetMap.put(symbol, asset);
            }
            return assetMap;
        }

/*        @Override
        public Map<String, Asset> mapRow(ResultSet resultSet, int i) throws SQLException {
            Map<String, Asset> assetMap = new TreeMap<>();
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                String symbol = resultSet.getString("symbol");
                String description = resultSet.getString("description");
                double cryptoPrice = resultSet.getDouble("cryptoPrice");
                Crypto crypto = new Crypto(name, symbol, description);
                double units = resultSet.getDouble("units");
                Asset asset = new Asset(crypto, units);
                assetMap.put(symbol, asset);
            }
            return assetMap;
        }*/
    }
}
