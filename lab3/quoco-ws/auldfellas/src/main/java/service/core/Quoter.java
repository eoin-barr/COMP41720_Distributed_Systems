package service.core;

import java.net.*;

import javax.jmdns.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import com.sun.net.httpserver.*;
import javax.jws.soap.SOAPBinding;
import java.util.concurrent.Executors;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService {
	// All references are to be prefixed with an AF (e.g. AF001000)
	public static final String PREFIX = "AF";
	public static final String COMPANY = "Auld Fellas Ltd.";

    public static void main(String[] args) {
        // Assign the host as an empty string
        String host = "";

        try {
            // If the length of the arguments is greater than 0 then set the host as the first argument
            // otherwise set it as 0.0.0.0 (broadcast address)
            host = args.length > 0 ? args[0] : "0.0.0.0";

            // Creating a new endpoint
            Endpoint quotation_endpoint = Endpoint.create(new Quoter());

            // Creating a new HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(9001), 5);

            // Creating a new thread pool
            server.setExecutor(Executors.newFixedThreadPool(5));

            // Creating a new context
            HttpContext ctx = server.createContext("/quotation");

            // Publishing the context to the endpoint
            quotation_endpoint.publish(ctx);

            // Starting the server
            server.start();

            // Attaching the host
            jmdnsAttach(host);

        } catch (Exception e) {
            // Printing the error if there is one
            e.printStackTrace();
        }
    }

    public static void jmdnsAttach(String host) {
        // If the host null then set it as 0.0.0.0
        host = host != null ? host : "0.0.0.0";

        // Setting the path
        String path = "path=/quotation?wsdl";

        try {
            JmDNS jm_DNS = JmDNS.create(InetAddress.getLocalHost());

            // Creating a new service
            ServiceInfo service_info = ServiceInfo.create("_http._tcp.local.", "afqs", 9001, path);

            // Registering the service
            jm_DNS.registerService(service_info);

            // Waiting for 33 seconds
            Thread.sleep(100000);

        } catch (Exception e) {
            // Printing the error message if there is one
            System.out.println("Problem advertising the service: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	/**
	 * Quote generation:
	 * 30% discount for being male
	 * 2% discount per year over 60
	 * 20% discount for less than 3 penalty points
	 * 50% penalty (i.e. reduction in discount) for more than 60 penalty points 
	 */
    @WebMethod
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 600 and 1200
		double price = generatePrice(600, 600);
		
		// Automatic 30% discount for being male
		int discount = (info.gender == ClientInfo.MALE) ? 30:0;
		
		// Automatic 2% discount per year over 60...
		discount += (info.age > 60) ? (2*(info.age-60)) : 0;
		
		// Add a points discount
		discount += getPointsDiscount(info);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getPointsDiscount(ClientInfo info) {
		if (info.points < 3) return 20;
		if (info.points <= 6) return 0;
		return -50;
		
	}

}
