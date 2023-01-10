package service.girlpower;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.core.Constants;
import service.core.BrokerService;
import service.core.QuotationService;

public class Server {
    public static void main(String[] args) {

        // Initialising variables
        QuotationService gpqService = new GPQService();
        String host = "localhost";

        try {
            Registry registry = null;
            if (args.length != 0) host = args[0];

            // Updating the host variable if the user has provided an argument
            registry = LocateRegistry.getRegistry(host, 1099);     

            // Export the stub for the Girlpower quotation service object
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(gpqService,0);

            // Register the quotation service with the broker
            BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
            brokerService.registerService(Constants.GIRL_POWER_SERVICE, quotationService);

            // Displaying that the server is working
            System.out.println("STOPPING SERVER SHUTDOWN");

            // Ensuring the thread is asleep between connections
            while (true) {Thread.sleep(1000); }

        } catch(Exception e){
            // Print error message if there is a problem
            System.out.println("Trouble: " + e);
        }
    }
}
