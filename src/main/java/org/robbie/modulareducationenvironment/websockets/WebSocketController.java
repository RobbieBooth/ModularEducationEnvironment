package org.robbie.modulareducationenvironment.websockets;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendData")  // Map client messages to this method
    @SendTo("/topic/updates")  // Broadcast the message to all subscribers of the "/topic/updates" channel
    public String sendData(String message) throws Exception {
        // Process the message, for example, updating the user or returning new data
        return "Server received: " + message;
    }

    // You can also send messages directly to specific users
    public void sendMessageToUser(String user, String message) {
        messagingTemplate.convertAndSendToUser(user, "/queue/updates", message);
    }
}

