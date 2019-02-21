package eu.smogura.blueprints.tiwder.services;

import eu.smogura.blueprints.tiwder.dataobjects.Message;
import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.dataobjects.UserPublicData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static eu.smogura.blueprints.tiwder.UserTestUtils.mockUserService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {
    @Mock
    private UsersService usersService;

    @InjectMocks
    private MessageService messageService = new MessageService();

    @Before
    public void beforeTest() {
        mockUserService(usersService, "1");
        mockUserService(usersService, "2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateNoUserId() {
        Message message = Message.builder().build();
        messageService.validate(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateUserDoesntExist() {
        Message message = Message.builder().postingUserId("not-existing-ig").build();
        messageService.validate(message);
    }

    @Test
    public void validateOk() {
        Message message = Message.builder().postingUserId("1").build();
        messageService.validate(message);
    }

    @Test
    public void post() {
        Message message = Message.builder()
            .postingUserId("1")
            .messageBody("Hello")
            .build();

        Message response = messageService.post(message);
        assertEquals("Hello", response.getMessageBody());
        assertNotNull(response.getMessageId());

        assertTrue(response.getPostDate() <= System.currentTimeMillis());

        //100 ms looks ok to check if posting time is set correctly
        assertTrue(response.getPostDate() >= System.currentTimeMillis() - 100);

    }

    @Test
    public void getMessages() throws InterruptedException {
        Message message1 = Message.builder()
            .postingUserId("2")
            .messageBody("Hello1")
            .build();

        Message message2 = Message.builder()
            .postingUserId("2")
            .messageBody("Hello2")
            .build();

        final User user = User.builder()
            .publicData(UserPublicData.builder()
                .userId("2")
                .build()
            ).build();

        messageService.post(message1);
        Thread.sleep(1); // Wait so next message will get new time
        messageService.post(message2);

        List<Message> messages = messageService.getMessages(user);

        assertEquals(2, messages.size());

        // Messages should came in reverse chronological order
        assertEquals("Hello2", messages.get(0).getMessageBody());
        assertEquals("Hello1", messages.get(1).getMessageBody());
    }
}