import service.actor.Client;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;

public class Main {
    public static void main(String[] args) {
        try {
            // Creating a new ActorSystem
            ActorSystem system = ActorSystem.create();
            // Creating a url which allow for communicating with the broker
            ActorSelection url = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
            // Initialising a new Client instance with the name client
            ActorRef ref = system.actorOf(Props.create(Client.class, url),  "client");
            // Sending a "start" message to the client which will get quotes from each of the services for 
            // each of the pre-defined clients from the broker
            ref.tell("start", null);
        } catch (Exception e) {
            // Hanlde any errors
            System.out.println(e.getMessage());
        }
    }    
}
