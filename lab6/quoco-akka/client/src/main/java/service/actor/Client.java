package service.actor;

import java.util.Map;
import java.util.HashMap;
import java.text.NumberFormat;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;

import service.core.Quotation;
import service.core.ClientInfo;

import service.messages.ApplicationRequest;
import service.messages.ApplicationResponse;
import service.core.QuotationService;


public class Client extends AbstractActor {
    
    // Declaring and initialising static variable int of type int
    private static int id = 0;
    // Declaring and initialising static variable application_cache of type Map<Integer, ClientInfo>
    private static Map<Integer, ClientInfo> application_cache = new HashMap<>();
    // Declaring public variable url of type ActorSelection
    public ActorSelection url;
    // Declaring public variable quotation_service of type QuotationService
    public QuotationService quotation_service;

    // Constructor which takes ActorSelection as a parameter and is used to initialise 
    // the url variable which will be used to communicate with the broker
    public Client(ActorSelection u) {
        this.url = u;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            // Handling request of type String
            .match(String.class,
                msg -> {
                    // If the message is not "start" return
                    if (!msg.equals("start")) return;
                    // Iterating over the clients
                    for (ClientInfo client : clients) {
                        try {
                            // Creating a new ApplicationRequest instance with a unique id and the client
                            ApplicationRequest req = new ApplicationRequest(id++, client);
                            // Adding the application id and client to the application_cache
                            application_cache.put(req.getApplicationId(), req.getClientInfo());
                            // Sending the request to the broker
                            url.tell(req, getSelf());
                        }catch(Exception e) {
                            // Handle any errors
                            e.printStackTrace();
                        }
                    }
                })
                .match(ApplicationResponse.class,
                    res -> {
                        try {
                            // Getting the client from the application_cache
                            ClientInfo client = application_cache.get(res.getApplicationId());
                            // Displaying the clients profile
                            displayProfile(client);
                            // Iterating over the AppliocationResponse quotations
                            for (Quotation q : res.getQuotations()) {
                                // Displaying the quotation
                                displayQuotation(q);
                            } 
                        }catch (Exception e) {
                            // Handle any errors
                            e.printStackTrace();
                        }
                    })
                    .build();
    }
            /**
	 * Display the client info nicely.
	 * 
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.getName()) + 
				" | Gender: " + String.format("%1$-27s", (info.getGender()==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.getAge())+" |");
		System.out.println(
				"| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) + 
				" | No Claims: " + String.format("%1$-24s", info.getNoClaims()+" years") +
				" | Penalty Points: " + String.format("%1$-19s", info.getPoints())+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 * 
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.getCompany()) + 
				" | Reference: " + String.format("%1$-24s", quotation.getReference()) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice()))+" |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")		
	};
}
