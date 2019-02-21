package eu.smogura.blueprints.tiwder.controllers;

import eu.smogura.blueprints.tiwder.dataobjects.Message;
import eu.smogura.blueprints.tiwder.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to manipulate messages by current user. Includes actions like
 * getUser, update, post or delete.
 */
@RestController
@RequestMapping(path = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public Message post(@RequestBody Message message) {
        return messageService.post(message);
    }
}
