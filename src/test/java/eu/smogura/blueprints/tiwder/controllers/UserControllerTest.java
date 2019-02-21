package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.dataobjects.UserPrivateData;
import eu.smogura.blueprints.tiwder.dataobjects.UserPublicData;
import eu.smogura.blueprints.tiwder.services.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UserController userController;

    private User sampleUser;
    @Before
    public void beforeTest() {
        sampleUser = User.builder()
            .privateData(UserPrivateData.builder().build())
            .publicData(UserPublicData.builder()
                .userId("1")
                .build()
            ).build();

        doReturn(Collections.singletonList(sampleUser))
            .when(usersService)
            .searchByName(anyString(), anyInt(), anyInt());

        doReturn(Optional.of(sampleUser))
            .when(usersService)
            .getById(eq("1"));

        lenient().doReturn(Optional.empty())
            .when(usersService)
            .getById(eq("not-existing-is"));
    }

    @Test
    public void searchByName() {
        List<User> userList = userController.searchByName("aaaa", 1, 10);
        verify(usersService).searchByName("aaaa", 1, 10);

        assertEquals(1, userList.size());
        assertEquals(sampleUser, userList.get(0));
    }

    @Test
    public void get() {
        ResponseEntity<User> userResponse = userController.getUser("1");
        verify(usersService).getById("1");

        assertEquals(200, userResponse.getStatusCodeValue());
        assertEquals(sampleUser, userResponse.getBody());
    }

    @Test
    public void getNotFound() {
        ResponseEntity<User> userResponse = userController.getUser("2");
        verify(usersService).getById("2");

        assertEquals(404, userResponse.getStatusCodeValue());
    }

    @Test
    public void createUser() {
        userController.createUser(sampleUser);

        verify(usersService).create(eq(sampleUser));
    }
}