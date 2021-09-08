/**
 * @Author Johnny Chan
 * Deze DAO-class haalt de crypto-assets uit de SQL-database.
 */
package com.miw.database;

import com.miw.model.Asset;
import com.miw.model.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class JdbcAssetDao {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(JdbcAssetDao.class);

    public JdbcAssetDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("JdbcAssetDAO-object created.");
    }


    public Asset getAssetBySymbol(int accountId, String symbol) {
        String sql = "SELECT a.symbol, name, cryptoPrice, units, description, dateRetrieved " +
                "FROM (Asset a JOIN Crypto c ON a.symbol = c.symbol) " +
                "JOIN CryptoPrice p ON p.symbol = c.symbol " +
                "WHERE accountID = ? AND a.symbol = ? AND dateRetrieved >= DATE_ADD(" +
                "   (SELECT dateRetrieved FROM CryptoPrice " +
                "   ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, CURRENT_TIMESTAMP)) LIMIT 1)" +
                "   , INTERVAL -10 SECOND)" +
                "AND dateRetrieved <= DATE_ADD(" +
                "   (SELECT dateRetrieved FROM CryptoPrice " +
                "   ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, CURRENT_TIMESTAMP)) LIMIT 1)" +
                "   , INTERVAL 10 SECOND);";
        return jdbcTemplate.queryForObject(sql, new AssetRowMapper(), accountId, symbol);
    }

    public List<Asset> getAssets(int accountId) {
        String sql = "SELECT a.symbol, name, cryptoPrice, units, description, dateRetrieved " +
                "FROM (Asset a JOIN Crypto c ON a.symbol = c.symbol) " +
                "JOIN CryptoPrice p ON p.symbol = c.symbol " +
                "WHERE accountID = ? AND dateRetrieved >= DATE_ADD(" +
                "   (SELECT dateRetrieved FROM CryptoPrice " +
                "   ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, CURRENT_TIMESTAMP)) LIMIT 1)" +
                "   , INTERVAL -10 SECOND)" +
                "AND dateRetrieved <= DATE_ADD(" +
                "   (SELECT dateRetrieved FROM CryptoPrice " +
                "   ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, CURRENT_TIMESTAMP)) LIMIT 1)" +
                "   , INTERVAL 10 SECOND);";
        return jdbcTemplate.query(sql, new AssetRowMapper(), accountId);
    }

    public Double getSymbolUnitsAtDateTime(int accountId, String symbol, LocalDateTime dateTime) {
        String sql = "SELECT (" +
                "   (SELECT units " +
                "   FROM Asset " +
                "   WHERE accountID = ? AND symbol = ?) " +
                "   +" +
                "   (SELECT IFNULL(SUM(units), 0) FROM `Transaction` " +
                "   WHERE accountID_seller = ? AND symbol = ? AND `date` BETWEEN ? AND CURRENT_TIMESTAMP()) " +
                "   - " +
                "   (SELECT IFNULL(SUM(units), 0) FROM `Transaction` " +
                "   WHERE accountID_buyer = ? AND symbol = ? AND `date` BETWEEN ? AND CURRENT_TIMESTAMP())) " +
                "AS unitsAtDateTime;";
        Double units = jdbcTemplate.queryForObject(sql, Double.class, accountId, symbol, accountId, symbol, dateTime,
                accountId, symbol, dateTime);
        return (units != null) ? units : 0.0;
    }


    private static class AssetRowMapper implements RowMapper<Asset> {

        @Override
        public Asset mapRow(ResultSet resultSet, int i) throws SQLException {
            Asset asset = null;
            String name = resultSet.getString("name");
            String symbol = resultSet.getString("symbol");
            String description = resultSet.getString("description");
            double cryptoPrice = resultSet.getDouble("cryptoPrice");
            Crypto crypto = new Crypto(name, symbol, description, cryptoPrice);
            double units = resultSet.getDouble("units");
            asset = new Asset(crypto, units);
            return asset;
        }
    }


    // ALTERNATIEF ALS DE WAARDEBEREKENINGEN BIJ AANMAAK VAN ASSETS IN ASSET CLASS ZELF GEBEUREN EN DE GEMAAKTE ASSETS DIRECT IN EEN MAP KUNNEN

    public Map<String, Asset> getAssetsMap(int accountId) {
        String sql = "SELECT accountID, a.symbol, `name`, symbol, cryptoPrice, `description`, units, `dateRetrieved` " +
                "FROM (Asset a JOIN Crypto c ON a.symbol = c.symbol) " +
                "JOIN CryptoPrice p ON p.symbol = c.symbol " +
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
    }


}
