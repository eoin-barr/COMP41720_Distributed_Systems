package service.core;

import java.net.*;
import java.util.*;
import java.io.IOException;

import javax.jmdns.*;
import javax.xml.ws.*;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class Broker {
    // Initialising the services urls as an empty linked list
    static LinkedList<URL> service_urls = new LinkedList<URL>();

    // Constructor when given a list of services
    public Broker(LinkedList<URL> services_init) {
        Broker.service_urls = services_init;
    }

    // Constructor when given no services
    public Broker(){}


    public static void main(String[] args) {
        try {
            // Publish the broker service on port 9000
            Endpoint.publish("http://0.0.0.0:9000/broker", new Broker(service_urls));

            // Create a JmDNS instance
            JmDNS jmdns_instance = JmDNS.create(InetAddress.getLocalHost());

            // Add a service listener on port 9000 which advertises on its existence on the network
            jmdns_instance.addServiceListener("_http._tcp.local.", new WSDLServiceListener());

            // Add a 10 second delay after the startup
            Thread.sleep(30000);

        } catch (IOException | InterruptedException e) {
            // Print the error message
            System.out.println(e.getMessage());
        }
    }

    // Service listener class
    public static class WSDLServiceListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            // Printing when a service is added 
            System.out.println("Service added: " + event.getInfo());
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            // Printing when a service is removed 
            System.out.println("Service removed: " + event.getInfo());
        }

        
        @Override
        public void serviceResolved(ServiceEvent event) {
            // Getting the url of the service
            String service_url = event.getInfo().getNiceTextString() != null ? event.getInfo().getURLs()[0] : "";

            // Getting the path of the service defined in each of the quotation services
            String path = event.getInfo().getPropertyString("path") != null ? event.getInfo().getPropertyString("path") : "/quotation?wsdl";

            try {
                // Concatenating the path to the service url
                service_url += path;

                System.out.println(service_url);

                // Adding the service url to the list of services
                service_urls.add(new URL(service_url));

            }catch(MalformedURLException e) {
                // Printing if an error occurs
                e.printStackTrace();
            }
        }
    }

    public List<Quotation> getQuotations(ClientInfo info) {
        // Assigning the variables for the processes
        List<Quotation> quotations = new LinkedList<Quotation>();

        // Iterating through the list of service urls
        for (URL wsdlUrl : service_urls) {
            // Creating a service name
            QName service_name = new QName("http://core.service/", "QuoterService");
            Service service = Service.create(wsdlUrl, service_name);

            // Creating a port name
            QName port_name = new QName("http://core.service/", "QuoterPort");
            QuoterService quotationService = service.getPort(port_name, QuoterService.class);

            // Add the quotation to the list of quotations to be returned to the client
            quotations.add(quotationService.generateQuotation(info));
        }

        // Return the list of quotations
        return quotations;
    }
}


