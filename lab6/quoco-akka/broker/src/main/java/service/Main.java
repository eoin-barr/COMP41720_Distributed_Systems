package service;

import service.actor.Broker;

import akka.actor.Props;
import akka.actor.ActorSystem;

public class Main {
    public static void main(String[] args) {
        // Creating the actor system
        ActorSystem system = ActorSystem.create();
        // Initialising an instance of the broker class with a name broker
        system.actorOf(Props.create(Broker.class), "broker");
    }    
}
