package service;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;

import service.actor.Init;
import service.actor.Quoter;
import service.girlpower.GPQService;

public class Main {
    public static void main(String[] args) {
        // Creating the actor system
        ActorSystem system = ActorSystem.create();
        // Creating the girlpower actor 
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "girlpower");
        // Initialising the GPQService
        ref.tell(new Init(new GPQService()), null);
        // Getting the broker actor
        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        // Registering girlpower with the broker
        selection.tell("register", ref);
    } 
}
