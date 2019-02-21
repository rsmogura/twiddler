package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.dataobjects.DisplayMessage;
import eu.smogura.blueprints.tiwder.dataobjects.Message;
import eu.smogura.blueprints.tiwder.services.MessageService;
import eu.smogura.blueprints.tiwder.services.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static eu.smogura.blueprints.tiwder.UserTestUtils.mockUserService;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WallControllerTest {
    @Mock
    private UsersService usersService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private WallController wallController;

    @Before
    public void setUp() throws Exception {
        mockUserService(usersService, "1");

        List<Message> messages = new ArrayList<>();
        messages.add(Message.builder().postingUserId("1").messageBody("1").build());
        messages.add(Message.builder().postingUserId("1").messageBody("2").build());

        doReturn(messages).when(messageService).getMessages(any());
    }

    @Test
    public void get() {
        List<DisplayMessage> displayMessages = wallController.get("1");

        verify(messageService).getMessages(argThat(
                u -> u.getPublicData().getUserId().equals("1")
        ));

        assertEquals(2, displayMessages.size());
        assertMessage(displayMessages.get(0), "1");
        assertMessage(displayMessages.get(1), "2");
    }

    private void assertMessage(DisplayMessage message, String messageBody) {
        assertEquals("1", message.getPostingUser().getUserId());
        assertEquals(messageBody, message.getMessage().getMessageBody());
    }
}