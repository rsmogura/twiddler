package eu.smogura.blueprints.tiwder.services;

import eu.smogura.blueprints.tiwder.dataobjects.Message;
import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.utils.MessageUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service to provide operations on messages.
 */
@Service
public class MessageService {
  private static final String NO_USER_WITH_ID = "No user with %s in system";

  private Map<String, Message> messageStorage = new ConcurrentHashMap<>();

  private Map<String, List<Message>> messageToUserIdIndex = new ConcurrentHashMap<>();

  @Autowired
  private UsersService usersService;

  /**
   * Validates message if matches criteria to save.
   *
   * @param message message to validate
   */
  protected void validate(Message message) {
    if (message.getPostingUserId() == null) {
      throw new IllegalArgumentException("No posting user id");
    }

    usersService.getById(message.getPostingUserId())
      .orElseThrow(() -> new IllegalArgumentException(
        String.format(NO_USER_WITH_ID, message.getPostingUserId())));
  }

  /**
   * Posts & stores message in storage.
   *
   * @param message the message to post
   * @return message with default values filled up
   */
  public Message post(Message message) {
    validate(message);

    final String messageId = UUID.randomUUID().toString();
    message.setMessageId(messageId);
    message.setPostDate(System.currentTimeMillis());

    messageStorage.put(messageId, message);
    messageToUserIdIndex
      .computeIfAbsent(message.getPostingUserId(), (userId) -> new CopyOnWriteArrayList<>())
      .add(message);

    return message;
  }

  /**
   * Get messages for given user in reverse chronological order
   *
   * @param user user to which getUser messages.
   */
  public List<Message> getMessages(User user) {
    List<Message> usersMessages = messageToUserIdIndex.getOrDefault(user.getPublicData().getUserId(), Collections.emptyList());
    Collections.sort(usersMessages, MessageUtils::compareByPostingDate);

    return usersMessages;
  }
}
