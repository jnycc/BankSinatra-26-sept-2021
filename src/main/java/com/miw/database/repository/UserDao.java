package com.miw.database.repository;

import com.miw.database.domain.User;

public interface UserDao {

  User save(User user);
  User findByUsername(String username);
}
