package service;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import service.message.*;
import service.core.Quotation;
import service.girlpower.GPQService;

public class Receiver {
    // Static instace service variable declaration
    static GPQService service = new GPQService();

    public static void main(String[] args) {
        // Initializing and assigning the host variable
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        try {
            // Creates a Connection Facroty on TCP port of ActiveMQ
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
            
            // Creates a connection and assigns the girlpower service id
            Connection connection = factory.createConnection();
            connection.setClientID("girlpower");

            // Get the connection to create the session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Creating the quotations queue
            Queue queue = session.createQueue("QUOTATIONS");
            // Creating the producer for the quotations queue
            MessageProducer producer = session.createProducer(queue); 

            // Create applications topic
            Topic topic = session.createTopic("APPLICATIONS");
            // Creating the consumer for the applications topic
            MessageConsumer consumer = session.createConsumer(topic);

            // Initiaite the connection
            connection.start();

            // Begin an infinite loop to always listen to the applications topic
            while (true) {
                try {

                    // Get the next message from the APPLICATION topic
                    Message message = consumer.receive();

                    // Check it is the right type of message
                    if (message instanceof ObjectMessage) {

                        // It’s an Object Message
                        Object content = ((ObjectMessage) message).getObject();

                        if (content instanceof QuotationRequestMessage) {

                            // It’s a Quotation Request Message
                            QuotationRequestMessage request = (QuotationRequestMessage) content;

                            // Generate a quotation and send a quotation response message…
                            Quotation quotation = service.generateQuotation(request.info);
                            Message response = session.createObjectMessage(new QuotationResponseMessage(request.id, quotation));
                            producer.send(response);
                        }
                    } else {
                        // Not the right type of message
                        System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
                    }

                } catch(Exception e) {
                    // Catching the exception
                    System.out.println( e);
                }
            } 
        } catch (Exception e) {
            // Catching the exception
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }   
    }   
}
