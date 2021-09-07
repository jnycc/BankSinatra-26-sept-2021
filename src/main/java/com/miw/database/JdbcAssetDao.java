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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
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

    private PreparedStatement insertAssetStatement (Asset asset, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Asset (cryptoID, units)" + "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, asset.getCrypto().getCryptoId());
        ps.setDouble(2, asset.getUnits());
        return ps;
    }

    private String getAssetsStatement() {
        String sql = "SELECT accountID, a.cryptoID, name, symbol, cryptoPrice, units, description, dateRetrieved " +
                "FROM (Asset a JOIN Crypto c ON a.cryptoID = c.cryptoID) " +
                "JOIN CryptoPrice p ON p.cryptoID = c.cryptoID " +
                "WHERE accountID = ? AND dateRetrieved >= DATE_ADD(" +
                "   (SELECT dateRetrieved FROM CryptoPrice " +
                "   ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, CURRENT_TIMESTAMP)) LIMIT 1)" +
                "   , INTERVAL -10 SECOND)" +
                "AND dateRetrieved <= DATE_ADD(" +
                "   (SELECT dateRetrieved FROM CryptoPrice " +
                "   ORDER BY ABS(TIMESTAMPDIFF(second, dateRetrieved, CURRENT_TIMESTAMP)) LIMIT 1)" +
                "   , INTERVAL 10 SECOND);";
        return sql;
    }

    public List<Asset> getAssets(int accountId) {
        return jdbcTemplate.query(getAssetsStatement(), new AssetRowMapper(), accountId);
    }

    public Asset getAssetBySymbol(int accountId, String symbol) {
        String sql = getAssetsStatement() + " AND WHERE symbol = " + symbol;
        return jdbcTemplate.queryForObject(sql, new AssetRowMapper(), accountId, symbol);
    }

    public Asset save(Asset asset) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertAssetStatement(asset, connection), keyHolder);
        int assetId = keyHolder.getKey().intValue();
        asset.setAssetId(assetId);
        logger.info("New asset has been saved to the database.");
        return asset;
    }

    public void updateAsset(int newUnits, int assetId){
        String updateQuery = "UPDATE Asset SET units = ? WHERE assetId = ?;";
        jdbcTemplate.update(updateQuery, newUnits, assetId);
    }

    private static class AssetRowMapper implements RowMapper<Asset> {

        @Override
        public Asset mapRow(ResultSet resultSet, int i) throws SQLException {
            Asset asset = null;
            int cryptoId = resultSet.getInt("cryptoID");
            String name = resultSet.getString("name");
            String symbol = resultSet.getString("symbol");
            String description = resultSet.getString("description");
            double cryptoPrice = resultSet.getDouble("cryptoPrice");
            Crypto crypto = new Crypto(name, symbol, description, cryptoPrice);
            crypto.setCryptoId(cryptoId);
            double units = resultSet.getDouble("units");
            asset = new Asset(crypto, units);
            return asset;
        }
    }


    // ALTERNATIEF ALS DE WAARDEBEREKENINGEN BIJ AANMAAK VAN ASSETS IN ASSET CLASS ZELF GEBEUREN EN DE GEMAAKTE ASSETS DIRECT IN EEN MAP KUNNEN

    public Map<String, Asset> getAssetsMap(int accountId) {
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
                int cryptoId = resultSet.getInt("cryptoID");
                String name = resultSet.getString("name");
                String symbol = resultSet.getString("symbol");
                String description = resultSet.getString("description");
                double cryptoPrice = resultSet.getDouble("cryptoPrice");
                Crypto crypto = new Crypto(name, symbol, description, cryptoPrice);
                crypto.setCryptoId(cryptoId);
                double units = resultSet.getDouble("units");
                Asset asset = new Asset(crypto, units);
                assetMap.put(symbol, asset);
            }
            return assetMap;
        }
    }


}
