package service.client;

import java.text.NumberFormat;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import service.core.Constants;
import service.core.Quotation;
import service.core.ClientInfo;
import service.core.BrokerService;

public class Main {
	/**
	 * This is the starting point for the application. Here, we must
	 * get a reference to the Broker Service and then invoke the
	 * getQuotations() method on that service.
	 *
	 * Finally, you should print out all quotations returned
	 * by the service.
	 *
	 * @param args
	 */
	public static void main(String[] args){
        // Initialising and assigning the host variable
		String host = "localhost";

		try {
            Registry registry = null;
            // Updating the host variable if the user has provided an argument
            if (args.length != 0) host = args[0];

            // Retrieving to the RMI registry already created by the broker
            registry = LocateRegistry.getRegistry(host, 1099);

			// Retrieving the broker service in the RMI registry
			BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

			// Looping over each of the defined clients
			for (ClientInfo info : clients) {
                // Displaying each client's pofile information
				displayProfile(info);

				// Retrieving the quotations from the broker and printing them
				for(Quotation quotation : brokerService.getQuotations(info)) {
					displayQuotation(quotation);
				}

				// Printing new lines between each client for better readability
				System.out.println("\n");
			}
		} catch (Exception e){
			System.out.println(e);
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