package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.services.UsersService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  @Autowired
  private UsersService usersService;

  @ApiOperation(value = "Search users by specified name part")
  @RequestMapping(method = GET)
  public List<User> searchByName(
          @RequestParam String namePattern,
          @RequestParam(defaultValue = "0") int pageNo,
          @RequestParam(defaultValue = "20") int pageSize) {
    return usersService.searchByName(namePattern, pageNo, pageSize)
      .stream()
      .peek(u -> u.setPrivateData(null))
      .collect(Collectors.toList());
  }

  @ApiOperation("Get user object")
  @RequestMapping(path = "/{userId}", method = GET)
  public ResponseEntity<User> getUser(@PathVariable("userId") String userId) {
    final Optional<User> user = usersService.getById(userId);

    return user
      .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @ApiOperation("Create a new user")
  @RequestMapping(method = POST)
  public User createUser(@RequestBody User user) {
    return usersService.create(user);
  }
}
