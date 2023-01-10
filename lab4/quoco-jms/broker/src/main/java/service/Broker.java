package service;

import java.util.HashMap;
import java.text.NumberFormat;

import javax.jms.Topic;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.Connection;
import javax.jms.ObjectMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ConnectionFactory;

import service.message.QuotationRequestMessage;
import service.message.ClientApplicationMessage;
import service.message.QuotationResponseMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Broker {

    // Static instance variable declaration and assignment
    static HashMap<Long, ClientApplicationMessage> cache = new HashMap<>();

    public static void main(String[] args) {

        try {

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

            // Assigning the broker id to the connection
            connection.setClientID("broker");

            // Creating the session from the connection
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            
            // Creating the requests queue 
            Queue reqs_queue = session.createQueue("REQUESTS");
            // Creating the requests consumer
            MessageConsumer reqs_consumer = session.createConsumer(reqs_queue);

            // Creating the responses queue
            Queue resps_queue = session.createQueue("RESPONSES");
            // Creating the responses producer
            MessageProducer resps_producer = session.createProducer(resps_queue);

            // Creating the quotes queue
            Queue quotes_queue = session.createQueue("QUOTATIONS");
            // Creating the quotations consumer
            MessageConsumer quotes_consumer = session.createConsumer(quotes_queue);

            // Creating the apps topic
            Topic apps_topic = session.createTopic("APPLICATIONS");
            // Creating the apps producer
            MessageProducer apps_producer = session.createProducer(apps_topic);
            
            // Begin the connection
            connection.start();

            new Thread(() -> {
                // Setting up the infinite loop to listen to the requests queue and send 
                // the message to the applications topic
                while(true){
                    try{

                        // Get the next message from the Requests queue
                        Message req_message = reqs_consumer.receive();

                        // Check it is the right type of message
                        if (req_message instanceof ObjectMessage) {

                            // It is an Object Message
                            Object content = ((ObjectMessage) req_message).getObject();

                            // Check if the content is of type QuotationRequestMessage
                            if (content instanceof QuotationRequestMessage) {

                                // It is a Quotation Request Message
                                QuotationRequestMessage quotation_req = (QuotationRequestMessage) content;
                                Message quotation_response_message = session.createObjectMessage(quotation_req);

                                // Check if the quotation request is in the cache
                                if(!cache.containsKey(quotation_req.id)) {

                                    // If not, add it to the cache
                                    cache.put(quotation_req.id, new ClientApplicationMessage(quotation_req.id , quotation_req.info));
                                }

                                // Send quotation request message on to applications topic
                                apps_producer.send(quotation_response_message);

                                // Sleep for 10 seconds to wait for service quotations 
                                // to be received in the second thread
                                Thread.sleep(10000);    

                                // Get the session to create an object message of the quotation request
                                Message last_res_sent = session.createObjectMessage(cache.get(quotation_req.id));

                                // Send the message back to the response queue
                                resps_producer.send(last_res_sent);
                            }

                            // Acknowledge the recepit of the request
                            req_message.acknowledge();

                        } else {
                            // It’s not an Object Message
                            System.out.println("Unknown received_res type: " + req_message.getClass().getCanonicalName());
                        }
                    } catch (Exception e){
                        // Handle any exceptions
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                // Setting up the infinite loop to listen to the quotations queue
                // and if receive quotation response forward to response queue
                while(true){
                    try{
                        // Retrieve the latest messahe from the quotes queue
                        Message received_res = quotes_consumer.receive();

                        // Check the received message is of type ObjectMessgae
                        if (received_res instanceof ObjectMessage) {

                            // It is an Object Message
                            Object content = ((ObjectMessage) received_res).getObject();

                            // Check if the content is of type QuotationResponseMessage
                            if (content instanceof QuotationResponseMessage) {

                                // It is a Quotation Response Message
                                QuotationResponseMessage res_obj = (QuotationResponseMessage) content;

                                // Check if the quotation response is in the cache
                                if(cache.get(res_obj.id) != null){

                                    // Create a temportaty client application message
                                    ClientApplicationMessage temp_client_app_message = cache.get(res_obj.id);

                                    // Add the message to its quotations array
                                    temp_client_app_message.quotations.add(res_obj.quotation);
                                }
                            }
                            // Acknowledge the recepit of the quotation response
                            received_res.acknowledge();
                        } else {
                            // It’s not an Object Message
                            System.out.println("Unknown received_res type: " + received_res.getClass().getCanonicalName());
                        }
                    } catch(Exception e){
                        // Handle any exceptions
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
}