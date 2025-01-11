package org.robbie.modulareducationenvironment.eventHandler;

//Template method

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // Use type name for identification
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"               // Use "type" in JSON payload to distinguish event types
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuizEvent.class, name = "QuizEvent"),
        @JsonSubTypes.Type(value = QuestionEvent.class, name = "QuestionEvent")
})
public abstract class GenericEvent {

}
