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
 * Implementation of Quotation Service for Dodgy Drivers Insurance Company
 *  
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService  {
	// All references are to be prefixed with an DD (e.g. DD001000)
	public static final String PREFIX = "DD";
	public static final String COMPANY = "Dodgy Drivers Corp.";

    public static void main(String[] args) {
        // Assign the host as an empty string
        String host = "";

        try {
            // If the length of the arguments is greater than 0 then set the host as the first argument
            // otherwise set it as 0.0.0.0 (broadcast address)
            host = args.length > 0 ?  args[0] : "0.0.0.0";

            // Creating a new endpoint
            Endpoint quotation_endpoint = Endpoint.create(new Quoter());

            // Creating a new HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(9003), 5);

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
            ServiceInfo service_info = ServiceInfo.create("_http._tcp.local.", "ddqs", 9003, path);

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
	 * 5% discount per penalty point (3 points required for qualification)
	 * 50% penalty for <= 3 penalty points
	 * 10% discount per year no claims
	 */
    @WebMethod
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 800 and 1000
		double price = generatePrice(800, 200);
		
		// 5% discount per penalty point (3 points required for qualification)
		int discount = (info.points > 3) ? 5*info.points:-50;
		
		// Add a no claims discount
		discount += getNoClaimsDiscount(info);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 10*info.noClaims;
	}

}
