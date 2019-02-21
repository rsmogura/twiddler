package eu.smogura.blueprints.tiwder.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static eu.smogura.blueprints.tiwder.UserTestUtils.mockUserService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserFollowServiceTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UserFollowService userFollowService;

    @Before
    public void beforeTest() {
        mockUserService(usersService, "a");
        mockUserService(usersService, "b");
        mockUserService(usersService, "c");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateUserNotExist() {
        userFollowService.validateUser("not-exist");
    }

    @Test
    public void validateUserExist() {
        userFollowService.validateUser("a");
    }

    @Test
    public void follow() {
        userFollowService.follow("a", "b");
    }

    @Test
    public void getFollowers() {
        Set<String> followers;

        userFollowService.follow("a", "b");
        followers = userFollowService.getFollowers("a");

        assertEquals(1, followers.size());
        assertTrue(followers.contains("b"));

        userFollowService.follow("a", "c");
        followers = userFollowService.getFollowers("a");

        assertEquals(2, followers.size());
        assertTrue(followers.contains("b"));
        assertTrue(followers.contains("c"));

        // Sanity test
        assertEquals(0, userFollowService.getFollowers("b").size());
        assertEquals(0, userFollowService.getFollowers("c").size());

    }
}