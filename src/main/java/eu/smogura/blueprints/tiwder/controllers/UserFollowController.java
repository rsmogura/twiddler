package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.services.UserFollowService;
import eu.smogura.blueprints.tiwder.services.UsersService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * REST Controller exposing API to manage user and who follows who.
 *
 * <br />
 *
 * "Follow" objects are kept as separate object to keep "users" thin,
 * and not to overload system.
 */
@RestController
@RequestMapping(path = "follows")
public class UserFollowController {

  @Autowired
  private UserFollowService followService;

  @ApiOperation("Makes user to follow other user")
  @RequestMapping(path = "/{followerUserId}/{followedUserId}", method = RequestMethod.PUT)
  public void follow(
    @ApiParam("The user which will follow (follower)")
    @PathVariable("followerUserId") String followerUserId,
    @ApiParam("The id of user to follow")
    @PathVariable("followedUserId") String followedUserId) {

    followService.follow(followerUserId, followedUserId);
  }
}
