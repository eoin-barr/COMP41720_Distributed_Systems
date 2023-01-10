import service.Quotation;
import service.ClientInfo;
import service.ClientApplication;

import java.util.Map;
import java.util.HashMap;
import java.text.NumberFormat;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class Client {

    // Declaring and assigning the SEED_ID variable
    private static long SEED_ID = 0;

    // Declaring and initialising a map to store the quotations
    private static Map<Long, ClientInfo> client_cache = new HashMap<>();

    public static void main(String[] args) {
        // Declaring and initialising the host variable
        String host = "localhost";
        // Declaring and initialising the port variable
        int port = 8085;
        // If the length of the args array is greater than 0 assign the
        // host variable to the first element of the args array
        if (args.length > 0) {
            host = args[0];
        }
        try {
            // Create a new RestTemplate
            RestTemplate rest_template = new RestTemplate();
            // Iterating over each of the clients in the clients array
            for (ClientInfo client: clients) {
                // Creating a new ClientApplication
                ClientApplication quotation_req = new ClientApplication(client, SEED_ID++);
                // Adding the client application to the client_cache map
                client_cache.put(quotation_req.getClientApplicationId(), quotation_req.getClientInfo());
                // Creating a new HttpEntity with the quotation request as the body
                HttpEntity<ClientApplication> req = new HttpEntity<>(quotation_req);
                // Sending the request to the server and storing the response in an Object
                Object quotation = rest_template.postForObject("http://"+host+":"+ port +"/applications", req,  ClientApplication.class);
                // Checking if the quotation is an instance of ClientApplication
                if (quotation instanceof ClientApplication) {
                    // If it is, cast it to a ClientApplication
                    ClientApplication quotation_res = (ClientApplication) quotation;
                    // Get the client application id
                    ClientInfo info  = client_cache.get(quotation_res.getClientApplicationId());
                    // Display the profile information
                    displayProfile(info);
                    // Iterate over each of the quotations and display them
                    for (Quotation q : quotation_res.getQuotations()) {
                        displayQuotation(q);
                    }
                    // Display a line break
                    System.out.println("\n");
                }
            } 
        } catch (Exception e) {
            // Handle any errors
            System.out.println("Error: " + e.getMessage());
        }  
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
