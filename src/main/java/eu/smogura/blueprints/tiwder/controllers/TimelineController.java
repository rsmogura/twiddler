package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.dataobjects.DisplayMessage;
import eu.smogura.blueprints.tiwder.dataobjects.Message;
import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.services.MessageService;
import eu.smogura.blueprints.tiwder.services.UserFollowService;
import eu.smogura.blueprints.tiwder.services.UsersService;
import eu.smogura.blueprints.tiwder.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller to provide list of messages to display for current user.
 *
 * <br />
 *
 * This controller is an entry point for application to present messages
 * for current user with are required details in one HTTP call, to provide better
 * user experience <br />
 *
 * In order to post or modify message use {@link MessageController}
 */
@RestController
@RequestMapping("timeline")
public class TimelineController {
  private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

  private static final long TIMEOUT = 1000;

  @Autowired
  private MessageService messageService;

  @Autowired
  private UserFollowService userFollowService;

  @Autowired
  private UsersService usersService;

  /**
   * Creates task to get users and cache them locally.
   *
   * @param usersIds id of users to get
   * @param usersOutputMap map which will contain all obtained users
   * @param userMessages the output list where to store messages from queried users
   *
   * @return list of posted future tasks
   */
  private List<Future<?>> createGetUserTasks(
          Set<String> usersIds,
          Map<String, User> usersOutputMap,
          List<Message> userMessages
  ) {
    return usersIds
            .stream()
            .map(userId -> EXECUTOR.submit(() ->
                    this.getUserAndMessages(userId, usersOutputMap, userMessages)))
            .collect(Collectors.toList());
  }

  /**
   * Waits for all user tasks to finish.
   *
   * @param usersTasks list of user tasks to wait for.
   */
  private void waitForUsersResponses(List<Future<?>> usersTasks) throws InterruptedException, ExecutionException, TimeoutException {
    // Don't use parallel streams
    for (Future<?> task : usersTasks) {
      // Wait for result from every task, when ends result will be in allFollowedUsers map
      task.get(TIMEOUT, TimeUnit.MILLISECONDS);
    }
  }

  /**
   * Queries for messages posted by users in the {@code users} list.
   *
   * @param users list of users to query for
   * @param outputMessages the list to which add messages
   */
  private void queryForMessages(Collection<User> users, List<Message> outputMessages) {
    users
      .stream()
      .map(user -> messageService.getMessages(user))
      .forEach(outputMessages::addAll);
  }

  /**
   * Gets users from storage and adds it to map. <br />
   *
   * This method is intended to be run in separate threads and return results
   * via given map.
   *  @param userId the user to query
   * @param usersMap the map to which put user
   * @param userMessages
   */
  private void getUserAndMessages(String userId, Map<String, User> usersMap, List<Message> userMessages) {
    usersService.getById(userId).ifPresent(user -> {
      usersMap.put(user.getPublicData().getUserId(), user);
      userMessages.addAll(messageService.getMessages(user));
    });


  }
  /**
   * Displays messages for specified user.
   *
   * @param userId user id to search for
   * @param pageNo the page number
   * @param pageSize the number of results per page
   * @return list of messages to display for given user
   */
  @RequestMapping(method = GET)
  public List<DisplayMessage> listMessagesForUser(
    @RequestParam String userId,
    @RequestParam(defaultValue = "0") int pageNo,
    @RequestParam(defaultValue = "20") int pageSize) throws InterruptedException, TimeoutException, ExecutionException {
    // TODO Add exception mapper on Spring level

    final Set<String> followers = userFollowService.getFollowers(userId);

    // Will contain list of followed users
    final Map<String, User> allFollowedUsers = new ConcurrentHashMap<>();

    // Will contain list of followed user messages
    final List<Message> followedUsersMessages = new ArrayList<>();

    // Let's do this in parallel to speed up search
    final List<Future<?>> usersTasks = createGetUserTasks(
            followers, allFollowedUsers, Collections.synchronizedList(followedUsersMessages));
    waitForUsersResponses(usersTasks);

    // Sort messages
    Collections.sort(followedUsersMessages, MessageUtils::compareByPostingDate);

    // And combine everything into list of display messages
    return followedUsersMessages
        .stream()
        .map(message -> new DisplayMessage(
                allFollowedUsers.get(message.getPostingUserId()).getPublicData(),
                message))
        .collect(Collectors.toList());
  }
}
