package service.actor;

import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

import akka.actor.ActorRef;
import akka.actor.AbstractActor;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import service.messages.RequestDeadline;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;
import service.messages.ApplicationRequest;
import service.messages.ApplicationResponse;

public class Broker extends AbstractActor {

    // Declaring and initialising static variable actor_refs of type ArrayList
    static List<ActorRef> actor_refs = new ArrayList<>();
    // Declaring and initialising static variable application_cache of type HashMap
    static HashMap<Integer, ApplicationResponse> application_cache = new HashMap<>();
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
            // Handling request of type String
            .match(String.class, 
                msg -> {
                    // If the message is not "start" return
                    if (!msg.equals("register")) return;
                    // If the message is "start" then print and add the sender to the actor_refs list
                    System.out.println(getSender());
                    actor_refs.add(getSender());
                })
            // Handling request of type ApplicationRequest
            .match(ApplicationRequest.class, 
                req -> {
                    try {
                        // If the application_cache does not contains the application_id then put the application_id and the application_response in the application_cache
                        if (!application_cache.containsKey(req.getApplicationId())) {
                            application_cache.put(req.getApplicationId(), new ApplicationResponse(req.getApplicationId(), req.getClientInfo()));
                        }
                        // Iterating over each of the actors in the actor_refs list
                        for (ActorRef actor_ref : actor_refs) {
                            // Sending a new QuotationRequest to the actor_ref with the application_id and the client_info
                            actor_ref.tell(new QuotationRequest(req.getApplicationId(), req.getClientInfo()), getSelf());
                        }
                        // Waiting for the quotations from each of the services
                        getContext().system().scheduler().scheduleOnce(
                            Duration.create(2, TimeUnit.SECONDS),
                            getSelf(),
                            new RequestDeadline(req.getApplicationId(), getSender()),
                            getContext().dispatcher(), null
                        );
                    } catch (Exception e) {
                        // Print the error
                        e.printStackTrace();
                    }
                })
            // Handling request of type QuotationResponse
            .match(QuotationResponse.class,
                res -> {
                    try {
                        // Checking if the application_cachet contains the application_id
                        if (application_cache.get(res.getId()) != null) {
                            // Get the applicatoin response from the application_cache
                            ApplicationResponse temp_res = application_cache.get(res.getId());
                            // Add the quotation response to the application response
                            temp_res.getQuotations().add(res.getQuotation());
                    }
                    }catch (Exception e) {
                        // Print the error
                        e.printStackTrace();
                    }
                })
            // Handling request of type RequestDeadline
            .match(RequestDeadline.class, 
                send_inst -> {
                    try {
                        // Get the application response from the application_cache
                        ApplicationResponse res_sent = application_cache.get(send_inst.getApplicationId());
                        // Check if the application exists
                        if (res_sent != null) {
                            // return the application response to the sender
                            send_inst.getActor().tell(res_sent, getSelf());
                        }
                    } catch (Exception e) {
                        // Print the error
                        e.printStackTrace();
                    }
                })
                .build();
    }
}
