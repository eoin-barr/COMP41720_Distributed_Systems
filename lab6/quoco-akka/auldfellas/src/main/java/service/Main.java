package service;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;

import service.actor.Init;
import service.actor.Quoter;
import service.auldfellas.AFQService;

public class Main {
    public static void main(String[] args) {
        // Creating the actor system
        ActorSystem system = ActorSystem.create();
        // Creating the auldfeallas actor
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "auldfellas");
        // Initialising the AFQService
        ref.tell(new Init(new AFQService()), null);
        // Getting the broker actor
        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        // Registering auldfelas with the broker
        selection.tell("register", ref);
    } 
}
