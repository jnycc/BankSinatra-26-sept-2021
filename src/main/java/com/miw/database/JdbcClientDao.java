package com.miw.database;
import com.miw.model.Address;
import com.miw.model.Client;
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
public class JdbcClientDao implements ClientDao {

  private final Logger logger = LoggerFactory.getLogger(JdbcClientDao.class);

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcClientDao(JdbcTemplate jdbcTemplate) {
    super();
    this.jdbcTemplate = jdbcTemplate;
    logger.info("New JdbcMemberDao");
  }

  private PreparedStatement insertClientStatement(Client client, Connection connection) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(
            "insert into User (email, password, salt, role, isBlocked, firstName, prefix, lastName, street, " +
                    "houseNumber, houseNumberExtension, zipCode, city, bsn, dateOfBirth) values (?, ?, ?, 'client', 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
    );
    ps.setString(1, client.getEmail());
    ps.setString(2, client.getPassword());
    ps.setString(3, client.getSalt());
    ps.setString(4, client.getFirstName());
    ps.setString(5, client.getPrefix());
    ps.setString(6, client.getLastName());
    ps.setString(7, client.getAddress().getStreet());
    ps.setInt(8, client.getAddress().getHouseNumber());
    ps.setString(9, client.getAddress().getHouseNumberExtension());
    ps.setString(10, client.getAddress().getZipCode());
    ps.setString(11, client.getAddress().getCity());
    ps.setInt(12, client.getBsn());
    ps.setDate(13, new java.sql.Date(client.getDateOfBirth().getTime()));
    //TODO: beginkapitaal toewijzen door accountDAO aan te roepen (INSERT INTO Account...)
    return ps;
  }

  @Override
  public Client save(Client client) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> insertClientStatement(client, connection), keyHolder);
    int userId = keyHolder.getKey().intValue();
    client.setUserId(userId);
    logger.info("New client has been saved to the database.");
    return client;
  }

  @Override
  public Client findByEmail(String email) {
    String sql = "SELECT * FROM User WHERE email = ?";
    try {
      return jdbcTemplate.queryForObject(sql, new ClientRowMapper(), email);
    } catch (EmptyResultDataAccessException e) {
      logger.info("User does not exist in the database");
      return null;
    }
  }

  private static class ClientRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet resultSet, int i) throws SQLException {
      int id = resultSet.getInt("userID");
      String email = resultSet.getString("email");
      String firstName = resultSet.getString("firstName");
      String prefix = resultSet.getString("prefix");
      String lastName = resultSet.getString("lastName");
      String salt = resultSet.getString("salt");
      String hash = resultSet.getString("password");
      String street = resultSet.getString("street");
      int houseNumber = resultSet.getInt("houseNumber");
      String houseNrExtension = resultSet.getString("houseNumberExtension");
      String zipCode = resultSet.getString("zipCode");
      String city = resultSet.getString("city");
      Address address = new Address(city, zipCode, street, houseNumber, houseNrExtension);
      int bsn = resultSet.getInt("bsn");
      Date dateOfBirth = resultSet.getDate("dateOfBirth");
      Client client = new Client(email, hash, firstName, prefix, lastName, dateOfBirth, bsn, address);
      client.setUserId(id);
      client.setSalt(salt);
      client.setPassword(hash);
      return client;
    }
  }
}
