// Created by huub
// Creation date 2021-07-08

package com.miw.database;
import com.miw.model.Client;
import com.miw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

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
  private PreparedStatement insertMemberStatement(User user, Connection connection) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(
        "insert into User (email, password, isAdmin, salt, isBlocked) values (?, ?, 0, 'test', 0)",
        Statement.RETURN_GENERATED_KEYS
    );
    ps.setString(1, user.getEmail());
    ps.setString(2, user.getPassword());
    //ps.setString()
    return ps;
  }

  @Override
  public User save(User user) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> insertMemberStatement(user, connection), keyHolder);
    return user;
  }

  @Override
  public Client findByEmail(String email) {
    //TODO: DAO schrijven
    String sql = "SELECT * FROM User WHERE email = ?";
    Client client = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
    return client;
  }


  private static class UserRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet resultSet, int i) throws SQLException {
      int id = resultSet.getInt("userID");
      String email = resultSet.getString("email");
      String firstName = resultSet.getString("firstName");
      String prefix = resultSet.getString("prefix");
      String lastName = resultSet.getString("lastName");
//      String street = resultSet.getString("street");
//      int houseNumber = resultSet.getInt("houseNumber");
//      String houseNrExtension = resultSet.getString("houseNumberExtension");
//      String zipCode = resultSet.getString("zipCode");
//      String city = resultSet.getString("city");
//      int bsn = resultSet.getInt("bsn");
      Date dateOfBirth = resultSet.getDate("dateOfBirth");
      Client client = new Client(email, firstName, prefix, lastName);//TODO: uitbreiden met meer sql-columns
      client.setUserId(id);
      return client;
    }
  }
}
