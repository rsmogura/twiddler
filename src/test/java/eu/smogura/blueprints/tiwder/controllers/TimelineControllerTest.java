package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.UserTestUtils;
import eu.smogura.blueprints.tiwder.dataobjects.DisplayMessage;
import eu.smogura.blueprints.tiwder.dataobjects.Message;
import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.dataobjects.UserPublicData;
import eu.smogura.blueprints.tiwder.services.MessageService;
import eu.smogura.blueprints.tiwder.services.UserFollowService;
import eu.smogura.blueprints.tiwder.services.UsersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static eu.smogura.blueprints.tiwder.UserTestUtils.mockUserService;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TimelineControllerTest {

  @Mock
  private UsersService usersService;

  @Mock
  private MessageService messageService;

  @Mock
  private UserFollowService userFollowService;

  @InjectMocks
  private TimelineController timelineController = new TimelineController();

  private Map<String, List<Message>> messageList = new HashMap<>();

  @Test
  public void getUserAndAddToMap() {
  }

  @Test
  public void listMessagesForUser() throws Exception{
    mockUserService(usersService,"1");
    mockUserService(usersService,"2");
    mockUserService(usersService,"3");

    Set<String> followers = new HashSet<>();
    followers.add("2");
    followers.add("3");

    doReturn(followers).when(userFollowService).getFollowers("1");

    mockUserMessage("2", 100);
    mockUserMessage("2", 200);
    mockUserMessage("3", 50);
    mockUserMessage("4", 600);


    doAnswer(inv -> messageList.get(((User) inv.getArgument(0)).getPublicData().getUserId()))
      .when(messageService)
      .getMessages(any());

    List<DisplayMessage> displayMessages = timelineController.listMessagesForUser("1", 0, 20);

    assertEquals(200, displayMessages.get(0).getMessage().getPostDate());
    assertEquals(100, displayMessages.get(1).getMessage().getPostDate());
    assertEquals(50, displayMessages.get(2).getMessage().getPostDate());

    assertEquals(3, displayMessages.size());
  }

  private void mockUserMessage(String userId, long postDate) {
    Message message = new Message();
    message.setPostDate(postDate);
    message.setMessageId(UUID.randomUUID().toString());
    message.setPostingUserId(userId);

    messageList.computeIfAbsent(userId, (u) -> new ArrayList<>()).add(message);
  }
}
