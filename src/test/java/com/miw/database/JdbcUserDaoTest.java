package com.miw.database;

import com.miw.model.Administrator;
import com.miw.model.Client;
import com.miw.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JdbcUserDaoTest {

    @Autowired
    private JdbcUserDao userDao;
    private final String existingClientEmail = "test1@test.com";
    private final String existingAdminEmail = "admin@banksinatra.nl";
    private final String nonExistingClientEmail = "-@-.nl";
    private final String CLIENT = "client";
    private final String ADMIN = "admin";
    private User existingClient;
    private User nonExistingClient;
    private User existingAdmin;

    @BeforeEach
    void setUp() {
        existingClient = userDao.getUserByEmail(existingClientEmail);
        existingAdmin = userDao.getUserByEmail(existingAdminEmail);
        nonExistingClient = userDao.getUserByEmail(nonExistingClientEmail);
    }

    @Test
    void getUserByEmail() {
        assertThat(existingClient).isNotNull();
        assertThat(existingAdmin).isNotNull();
        assertThat(nonExistingClient).isNull();
    }

    @Test
    void getRoleByEmail() {
        assertThat(existingClient).isInstanceOf(Client.class);
        assertThat(existingAdmin).isInstanceOf(Administrator.class);
    }

    @Test
    void getIDByEmail() {
        assertThat(existingClient.getUserId()).isNotEqualTo(0);
        assertThat(existingAdmin.getUserId()).isNotEqualTo(0);
        assertThat(userDao.getIDByEmail(nonExistingClientEmail)).isEqualTo(0);
    }

    @Test
    void testGetRoleByEmail() {
        assertThat(userDao.getRoleByEmail(existingClientEmail)).isEqualTo(CLIENT);
        assertThat(userDao.getRoleByEmail(existingAdminEmail)).isEqualTo(ADMIN);
        assertThat(userDao.getRoleByEmail(nonExistingClientEmail)).isNull();
    }

    @Test
    void testGetRoleByID() {
        int clientId = existingClient.getUserId();
        int admindId = existingAdmin.getUserId();
        assertThat(userDao.getRoleByID(clientId)).isEqualTo(CLIENT);
        assertThat(userDao.getRoleByID(admindId)).isEqualTo(ADMIN);
    }

    @Test
    void getFirstNameById() {
        assertThat(userDao.getFirstNameById(existingAdmin.getUserId())).isEqualTo("Frank");
    }

    @Test
    void toggleBlock() {
        int adminId = existingAdmin.getUserId();
        int clientId = existingClient.getUserId();

        userDao.toggleBlock(true, adminId);
        userDao.toggleBlock(false, adminId);

        assertThat(userDao.checkIfBlockedByID(adminId)).isFalse();

        userDao.toggleBlock(true, clientId);
        userDao.toggleBlock(false, clientId);

        assertThat(userDao.checkIfBlockedByID(clientId)).isFalse();
    }
}