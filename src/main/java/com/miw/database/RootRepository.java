
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

  private ClientDao clientDAO;

  @Autowired
  public RootRepository(ClientDao clientDAO) {
    super();
    this.clientDAO = clientDAO;
    logger.info("New RootRepository");
  }

  public Client saveUser(Client client) {
    return clientDAO.save(client);
  }

  public User findByEmail(String email) {
      return clientDAO.findByEmail(email);
    }
}
