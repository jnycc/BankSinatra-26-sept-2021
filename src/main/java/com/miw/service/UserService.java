// Created by huub
// Creation date 2021-08-17

package com.miw.service;

import com.miw.model.Administrator;
import com.miw.model.Client;
import com.miw.model.User;
import com.miw.database.RootRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private RootRepository rootRepository;
  private User user;

  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  public UserService(RootRepository rootRepository) {
    super();
    this.rootRepository = rootRepository;
    logger.info("New MemberService");
  }

  //TODO: parameters aanpassen voor onze app - hoe registreert een nieuwe gebruiker zich?
  //TODO: LW-If User becomes abstract it cannot be instantiated. new User arg becomes invalid.
  public Client register(String emailaddress, String password) {
    Client client = new Client(emailaddress, password);       //  TODO: alle attributen toevoegen.
    rootRepository.saveUser(client);
    return client;
  }

  public RootRepository getRootRepository() {
    return rootRepository;
  }
}
