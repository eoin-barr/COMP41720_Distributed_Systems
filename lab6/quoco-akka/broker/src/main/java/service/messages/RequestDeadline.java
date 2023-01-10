package service.messages;

import akka.actor.ActorRef;

public class RequestDeadline {

    // Declaring private variable application_id of type int
    private int application_id;
    // Declaring private variable actor of type ActorRef
    private ActorRef actor;

    // Constructor for RequestDeadline class that initialise the private variables
    public RequestDeadline(int id, ActorRef actor) {
        this.application_id = id;
        this.actor = actor;
    }

    // Getter method for application_id
    public int getApplicationId() {
        return this.application_id;
    }

    // Setter method for application_id
    public void setApplicationId(int id) {
        this.application_id = id;
    }

    // Getter method for actor
    public ActorRef getActor() {
        return this.actor;
    }

    // Setter method for actor
    public void setActor(ActorRef actor) {
        this.actor = actor;
    }
}
