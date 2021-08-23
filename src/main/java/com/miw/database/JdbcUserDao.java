// Created by huub
// Creation date 2021-07-08

package com.miw.database;
import com.miw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
    return null;
  }

}
