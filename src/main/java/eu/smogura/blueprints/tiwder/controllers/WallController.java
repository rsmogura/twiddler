package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.dataobjects.DisplayMessage;
import eu.smogura.blueprints.tiwder.dataobjects.Message;
import eu.smogura.blueprints.tiwder.dataobjects.User;
import eu.smogura.blueprints.tiwder.services.MessageService;
import eu.smogura.blueprints.tiwder.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/wall")
public class WallController {
  @Autowired
  private UsersService usersService;

  @Autowired
  private MessageService messageService;

  @RequestMapping(method = RequestMethod.GET)
  public List<DisplayMessage> get(@RequestParam String userId) {
    final User user = usersService.getById(userId).orElseThrow(() -> new IllegalArgumentException("No such user"));

    // Service will returns messages in reverse chronological order
    return messageService.getMessages(user)
      .stream()
      .map(message -> new DisplayMessage(user.getPublicData(), message))
      .collect(Collectors.toList());
  }
}
