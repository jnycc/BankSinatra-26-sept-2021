package com.miw.service;

import com.miw.model.User;
import com.miw.database.RootRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private RootRepository mockRepo;
    private UserService userService;

    @BeforeEach
    public void setup(){
        mockRepo = Mockito.mock(RootRepository.class);
        userService = new UserService(mockRepo);
    }

    @AfterAll
    public void tearDown(){
        mockRepo = null;
        userService = null;
    }

    @Test
    void register() {
        //TODO: test aanpassen als we iets interessanters kunnen testen

        //User testUser = new User("testje", "welkom123");
        //Mockito.when(mockRepo.saveUser(testUser)).thenReturn(testUser);

        String username = "test";
        String password = "ookTest";
        User testUser = userService.register(username, password);
        assertThat(testUser.getEmailaddress()).isEqualTo("test");

    }

    @Test
    void generatePassword() {
    }
}