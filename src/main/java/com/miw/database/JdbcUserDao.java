package com.miw.database;
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
public class JdbcUserDao implements UserDao {

  private final Logger logger = LoggerFactory.getLogger(JdbcUserDao.class);

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcUserDao(JdbcTemplate jdbcTemplate) {
    super();
    this.jdbcTemplate = jdbcTemplate;
    logger.info("New JdbcMemberDao");
  }

  //TODO: Zorgen dat hij de salt doorgeeft aan de database
  //TODO: LW-SQL statement aanpassen aan nieuwe User entiteit
  private PreparedStatement insertMemberStatement(Client client, Connection connection) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(
            "insert into User (email, password, salt, role, isBlocked, firstName, prefix, lastName, street, " +
                    "houseNumber, houseNumberExtension, zipCode, city, bsn, dateOfBirth) values (?, ?, ?, 'client', 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
                "houseNumber, houseNumberExtension, zipCode, city, bsn, dateOfBirth) values (?, ?, ?, 'client', 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
    ps.setDate(13, java.sql.Date.valueOf(client.getDateOfBirth()));
    return ps;
  }

  @Override
  public Client save(Client client) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> insertMemberStatement(client, connection), keyHolder);
    int userId = keyHolder.getKey().intValue();
    client.setUserId(userId);
    return client;
  }

  @Override
  public Client findByEmail(String email) {
    String sql = "SELECT * FROM User WHERE email = ?";
    try {
      return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
    } catch (EmptyResultDataAccessException e) {
      logger.info("User bestaat niet");
      return null;
    }
  }

  private static class UserRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet resultSet, int i) throws SQLException {
      int id = resultSet.getInt("userID");
      String email = resultSet.getString("email");
      String firstName = resultSet.getString("firstName");
      String prefix = resultSet.getString("prefix");
      String lastName = resultSet.getString("lastName");
      String salt = resultSet.getString("salt");
      String hash = resultSet.getString("password");
//      String street = resultSet.getString("street");
//      int houseNumber = resultSet.getInt("houseNumber");
//      String houseNrExtension = resultSet.getString("houseNumberExtension");
//      String zipCode = resultSet.getString("zipCode");
//      String city = resultSet.getString("city");
//      int bsn = resultSet.getInt("bsn");
//      Date dateOfBirth = resultSet.getDate("dateOfBirth");
      Client client = new Client(email, firstName, prefix, lastName);//TODO: uitbreiden met meer sql-columns
      client.setUserId(id);
      client.setSalt(salt);
      client.setPassword(hash);
      return client;
    }
  }
}
