package org.robbie.modulareducationenvironment.eventHandler;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EventController {

    @MessageMapping("/send")
    @SendTo("/topic/event")
    public String sendMessage(String message) {
        System.out.println("Received message: " + message);
        return message;
    }
}

