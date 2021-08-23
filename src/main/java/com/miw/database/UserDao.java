package com.miw.database;

import com.miw.model.User;

public interface UserDao {

  User save(User user);
  User findByUsername(String username);
}
