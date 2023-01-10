package service.actor;

import akka.actor.AbstractActor;

import service.core.Quotation;
import service.core.QuotationService;

import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class Quoter extends AbstractActor {
    // Declaring private variable service of type QuotationService
    private QuotationService service;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            // Handling QuotationRequest messages
            .match(QuotationRequest.class,
                msg -> {
                    // Generating a quotation from the QuotationService
                    Quotation quotation = service.generateQuotation(msg.getClientInfo());
                    // Sending the quotation back to the sender
                    getSender().tell(new QuotationResponse(msg.getId(), quotation), getSelf());
            })
            // Handling Init messages
            .match(Init.class, 
            msg -> {
                // Initialising the QuotationService
                service = msg.getQuotationService();
            })
        .build();
    }
} 