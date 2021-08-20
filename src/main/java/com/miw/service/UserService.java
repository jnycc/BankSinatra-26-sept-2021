// Created by huub
// Creation date 2021-08-17

package com.miw.service;

import com.miw.database.domain.User;
import com.miw.database.repository.RootRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private RootRepository rootRepository;

  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  public UserService(RootRepository rootRepository) {
    super();
    this.rootRepository = rootRepository;
    logger.info("New MemberService");
  }

  //TODO: parameters aanpassen voor onze app - hoe registreert een nieuwe gebruiker zich?
  public User register(String username, String password) {
    User user = new User(username, password);
    rootRepository.saveUser(user);
    return user;
  }

  public RootRepository getRootRepository() {
    return rootRepository;
  }
}
