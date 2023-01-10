package service.core;

import java.net.*;

import javax.jws.WebMethod;
import javax.jmdns.*;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import com.sun.net.httpserver.*;
import javax.jws.soap.SOAPBinding;
import java.util.concurrent.Executors;

/**
 * Implementation of the Girl Power insurance quotation service.
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService {
	// All references are to be prefixed with an DD (e.g. DD001000)
	public static final String PREFIX = "GP";
	public static final String COMPANY = "Girl Power Inc.";


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
            HttpServer server = HttpServer.create(new InetSocketAddress(9002), 5);

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
            ServiceInfo service_info = ServiceInfo.create("_http._tcp.local.", "gpqs", 9002, path);
            
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
	 * 50% discount for being female
	 * 20% discount for no penalty points
	 * 15% discount for < 3 penalty points
	 * no discount for 3-5 penalty points
	 * 100% penalty for > 5 penalty points
	 * 5% discount per year no claims
	 */
	@WebMethod
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 600 and 1000
		double price = generatePrice(600, 400);
		
		// Automatic 50% discount for being female
		int discount = (info.gender == ClientInfo.FEMALE) ? 50:0;
		
		// Add a points discount
		discount += getPointsDiscount(info);
		
		// Add a no claims discount
		discount += getNoClaimsDiscount(info);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 5*info.noClaims;
	}

	private int getPointsDiscount(ClientInfo info) {
		if (info.points == 0) return 20;
		if (info.points < 3) return 15;
		if (info.points < 6) return 0;
		return -100;
		
	}

}
