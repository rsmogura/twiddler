package eu.smogura.blueprints.tiwder.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserFollowService {
  /** Follows storage. */
  private Map<String, Set<String>> followStorage = new ConcurrentHashMap<>();

  @Autowired
  private UsersService usersService;

  /**
   * Checks if user exists.
   *
   * @param userId id of user to check.
   */
  protected void validateUser(String userId) {
    usersService.getById(userId).orElseThrow(()
      -> new IllegalArgumentException("User " + userId + " doesn't exist"));
  }

  /**
   * Stores follow relation between two users. Relation is unidirectional.
   *
   * @param followerUserId the user which will follow
   * @param followedUserId followed user
   */
  public void follow(@NonNull String followerUserId, @NonNull String followedUserId) {
    validateUser(followerUserId);
    validateUser(followedUserId);

    if (followedUserId.equals(followerUserId)) {
      throw new IllegalArgumentException("Users can't follow self");
    }

    followStorage
      .computeIfAbsent(followerUserId, u -> Collections.synchronizedSet(new HashSet<>()))
      .add(followedUserId);
  }

  /**
   * Returns list of users following given user.
   *
   * @param userId the user for which to display followers
   * @return followers of given user.
   */
  public Set<String> getFollowers(String userId) {
    return followStorage.getOrDefault(userId, Collections.emptySet());
  }
}
