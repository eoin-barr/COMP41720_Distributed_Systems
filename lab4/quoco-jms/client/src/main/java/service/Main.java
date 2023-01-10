package service;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.Map;
import java.util.HashMap;

import service.core.Quotation;
import service.core.ClientInfo;
import service.message.QuotationRequestMessage;
import service.message.ClientApplicationMessage;

import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.Connection;
import java.text.NumberFormat;
import javax.jms.ObjectMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ConnectionFactory;

public class Main {
    // Static instance variable SEED_ID declaration and assignment
    private static long SEED_ID = 0;

    // Static instance variable cache declaration and assignment
    private static Map<Long, ClientInfo> cache = new HashMap<>();

    public static void main(String[] args) throws Exception {

        // Declaring and assigning the host and port vairiables
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        int port = 61616;

            // Iterate thourgh the args and set the host and port if they are given and handle 
            for (int i=0; i < args.length; i++) {
                switch (args[i]) {
                    case "-h":
                        host = args[++i];
                        break;
                    case "-p":
                        port = Integer.parseInt(args[++i]);
                        break;
                    default:
                        System.out.println("Unknown flag: " + args[i] +"\n");
                        System.out.println("Valid flags are:");
                        System.out.println("\t-h <host>\tSpecify the hostname of the target service");
                        System.out.println("\t-p <port>\tSpecify the port number of the target service");
                        System.exit(0);
                }
            }

        // Creating the connection factory on TCP port of ActiveMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":" + port + "");

        // Creating the factory connection
        Connection connection = factory.createConnection();

        // Assigning the client id to the connection
        connection.setClientID("client");

        // Creating the session from the connection
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // Creating the requests queue 
        Queue reqs_queue = session.createQueue("REQUESTS");
        // Creating the requests producer
        MessageProducer reqs_producer = session.createProducer(reqs_queue);
        
        // Creating the responses queue
        Queue resps_queue = session.createQueue("RESPONSES");
        // Creating the responses consumer
        MessageConsumer resps_consumer = session.createConsumer(resps_queue);

        // Begin the connection
        connection.start();

        new Thread(() -> {
            // Setting up the infinite loop to listen to the response queue for quotes
            while (true){
                try{

                    // Retrieve the latest messahe from the repsonse queue
                    Message last_message = resps_consumer.receive();

                    // Check the latest message is of type ObjectMessgae
                    if (last_message instanceof ObjectMessage) {

                        // It is an Object Message
                        Object content = ((ObjectMessage) last_message).getObject();

                        // Check if the content is of type ClientApplicationMessage
                        if (content instanceof ClientApplicationMessage) {

                            // It is a ClientApplicationMessage
                            ClientApplicationMessage last_response = (ClientApplicationMessage) content;

                            // Retrieve the client info from the cache
                            ClientInfo info = cache.get(last_response.client_app_message_id);

                            // Print the client's info
                            displayProfile(info);

                            // Iterate through the quotations array and display the quotations
                            for (Quotation quotation: last_response.quotations ){
                                displayQuotation(quotation);
                            }

                            // Print out a new line for better visibility
                            System.out.println("\n");
                        }
                        // Acknowledge the recepit of the latest message
                        last_message.acknowledge();

                    } else {
                        // It’s not an Object Message
                        System.out.println("Unknown message type: " + last_message.getClass().getCanonicalName());
                    }
                } catch (Exception e) {
                    // Handle any exceptions
                    e.printStackTrace();
                }
            }
        }).start();

        // Iterate through the test data
        for (ClientInfo info : clients) {
            try {
                // Create a new request message
                QuotationRequestMessage first_request = new QuotationRequestMessage(SEED_ID++, info);
                
                // Use the session to create an object message from the request message
                Message request = session.createObjectMessage(first_request);
                
                // Add the client info to the cache
                cache.put(first_request.id, first_request.info);
                
                // Send the request to the request producer
                reqs_producer.send(request);

            } catch (Exception e){
                // Handle any exceptions
                e.printStackTrace();
            }
        }
        // Begin the connection
        connection.start();
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
				"| Name: " + String.format("%1$-29s", info.name) + 
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| License Number: " + String.format("%1$-19s", info.licenseNumber) + 
				" | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
				" | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
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
				"| Company: " + String.format("%1$-26s", quotation.company) + 
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
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