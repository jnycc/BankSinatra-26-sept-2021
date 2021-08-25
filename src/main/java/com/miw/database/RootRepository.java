
package com.miw.database;

import com.miw.model.Client;
import com.miw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RootRepository {

  private final Logger logger = LoggerFactory.getLogger(RootRepository.class);

  private UserDao userDao;

  @Autowired
  public RootRepository(UserDao userDao) {
    super();
    this.userDao = userDao;
    logger.info("New RootRepository");
  }

  public Client saveUser(Client client) {
    return userDao.save(client);
  }

    public User findByEmail(String email) {
      return userDao.findByEmail(email);
    }
}
