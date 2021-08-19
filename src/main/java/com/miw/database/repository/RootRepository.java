// Created by huub
// Creation date 2021-07-08

package com.miw.database.repository;

import com.miw.database.domain.User;
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

  public User saveUser(User user) {
    return userDao.save(user);
  }
}
