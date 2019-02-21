package eu.smogura.blueprints.tiwder;

import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.dataobjects.UserPublicData;
import eu.smogura.blueprints.tiwder.services.UsersService;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

public class UserTestUtils {
    public static void mockUserService(UsersService usersServiceMock, String s) {
        lenient().doReturn(Optional.of(User.builder().publicData(new UserPublicData(s, s, s)).build()))
                .when(usersServiceMock).getById(s);
    }
}
