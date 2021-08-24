// Created by huub
// Creation date 2021-07-08

package com.miw.database;
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
        "insert into Gebruiker (gebruikersnaam, wachtwoord, isBeheerder, salt, isGeblokkeerd) values (?, ?, 0, 'test', 0)",
        Statement.RETURN_GENERATED_KEYS
    );
    ps.setString(1, user.getEmailaddress());
    ps.setString(2, user.getPassword());
    return ps;
  }

  @Override
  public User save(User user) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> insertMemberStatement(user, connection), keyHolder);
    return user;
  }

  @Override
  public User findByUsername(String username) {
    //TODO: DAO schrijven
    String sql = "SELECT * FROM User WHERE username = ?";
    User user = jdbcTemplate.queryForObject(sql, new UserRowMapper)
    return user;


    //jdbcTemplate.query returnt een lijst, daarom List van Messages, ookal haal je maar 1 message eruit.
    //List<Message> messages = jdbcTemplate.query("SELECT * FROM message_table WHERE id = ?", new MessageRowMapper(), id);
    //if (messages.size() == 1) return messages.get(0);
    //return null;
  }


  private static class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
      String id = resultSet.getString("userID");
      String email = resultSet.getString("username");
      String firstName = resultSet.getString("firstName");
      String prefix = resultSet.getString("prefix");
      String lastName = resultSet.getString("lastName");
      User user = new User();
      user.setId(id);
      return user;
    }
  }
}
