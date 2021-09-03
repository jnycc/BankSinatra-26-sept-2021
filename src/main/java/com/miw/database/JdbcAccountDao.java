package com.miw.database;

import com.miw.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class JdbcAccountDao {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(JdbcAccountDao.class);

    @Autowired
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private PreparedStatement insertAccountStatement(Account account, int userId, Connection connection) throws SQLException {
        String sql = "INSERT INTO Account (IBAN, balance, userID) VALUES(?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, account.getIban());
        preparedStatement.setDouble(2, account.getBalance());
        preparedStatement.setInt(3, userId);
        return preparedStatement;
    }

    public Account save(Account account, int userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertAccountStatement(account, userId, connection), keyHolder);
        int accountId = keyHolder.getKey().intValue();
        account.setAccountId(accountId);
        return account;
    }

    public void updateBalance(double newBalance, int accountId){
        String updateQuery = "UPDATE Account SET balance = ? WHERE accountID = ?;";
        jdbcTemplate.update(updateQuery, newBalance, accountId);
    }

    public Account getAccountById(int accountId){
        String sql = "SELECT * FROM Account WHERE accountID = ?;";
        return jdbcTemplate.queryForObject(sql, new JdbcAccountDao.AccountRowMapper(), accountId);
    }

    public Account getAccountByEmail(String email) {
        String sql = "SELECT * FROM Account WHERE userID = ( SELECT userID FROM user WHERE email = ?);";
        try {
            return jdbcTemplate.queryForObject(sql, new JdbcAccountDao.AccountRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Account does not exist in the database");
            return null;
        }
    }

    private static class AccountRowMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet resultSet, int i) throws SQLException {
            int accountId  = resultSet.getInt("accountID");
            String iban  = resultSet.getString("IBAN");
            double balance = resultSet.getDouble("balance");
            Account account = new Account(balance);
            account.setIban(iban);
            account.setAccountId(accountId);
            return account;
        }
    }

}
