package org.robbie.modulareducationenvironment.moduleHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
/**
 * Used by modules to message specific quizzes over websockets.
 * Ie if a user sends an event to the server and it takes a while to process the module would call a function on this service to send the updated question data to the user.
 */
public class ModuleMessagingService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ModuleMessagingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Send a question update for a quiz attempt.
     *
     * @param quizAttemptUUID The quiz attempt that is being attempted
     * @param questionData The question data
     */
    public void sendQuestionUpdate(UUID quizAttemptUUID, Map<String, Object> questionData) {
        String userDestination = "/quiz/" + quizAttemptUUID.toString() +"/event";
        messagingTemplate.convertAndSend(userDestination, questionData);
    }
}

