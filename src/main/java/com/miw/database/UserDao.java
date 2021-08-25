package com.miw.database;

import com.miw.model.Client;
import com.miw.model.User;

public interface UserDao {

  Client save(Client client);
  User findByEmail(String email);
}
