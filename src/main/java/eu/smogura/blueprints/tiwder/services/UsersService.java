package eu.smogura.blueprints.tiwder.services;

import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.dataobjects.UserPrivateData;
import eu.smogura.blueprints.tiwder.dataobjects.UserPublicData;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * User service - responsible for manipulating user data.
 */
@Service
public class UsersService {
  private static final Map<String, User> usersStorage = new ConcurrentHashMap<>();

  /**
   * Search for user names containing {@code namePart}.
   *
   * @param namePart the part of name
   * @param pageNo the number of page with results
   * @param pageSize how many results return in one call
   *
   * @return list with users matching given criteria.
   */
  public List<User> searchByName(@NonNull String namePart, int pageNo, int pageSize) {
    // Normalize query string
    final String namePartNormalized = namePart.toLowerCase();

    // Right now no need to take care for paging, as it will be differently implemented
    // with real storage
    return usersStorage.values()
      .stream()
      .filter(u -> u.getPublicData().getUserName().toLowerCase().contains(namePartNormalized))
      .collect(Collectors.toList());
  }

  /**
   * Searches for user by given id.
   *
   * @param id the user id
   * @return the user id or empty optional if user not found.
   */
  public Optional<User> getById(String id) {
    return Optional.ofNullable(usersStorage.get(id));
  }

  /**
   * Creates a new user and store it in storage.
   *
   * @param user the user to create to
   * @return the user which has been created with default values set.
   */
  public User create(@NonNull User user) {
    final UserPublicData userPublicData = user.getPublicData();

    if (userPublicData == null) {
      // TODO Add more validations with better error messages
      throw new IllegalStateException("Object is missing basic info");
    }

    // Small workaround for demo to keep test.sh simpler
    if (userPublicData.getUserId() == null) {
      userPublicData.setUserId(UUID.randomUUID().toString());
    }

    usersStorage.put(userPublicData.getUserId(), user);

    return user;
  }
}
