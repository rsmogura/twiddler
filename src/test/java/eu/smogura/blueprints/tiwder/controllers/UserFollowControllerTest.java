package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.services.UserFollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserFollowControllerTest {
    @Mock
    private UserFollowService userFollowService;

    @InjectMocks
    private UserFollowController userFollowController = new UserFollowController();

    @Test
    public void follow() {
        userFollowController.follow("a", "b");
        verify(userFollowService).follow("a", "b");
    }
}