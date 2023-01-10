package service.broker;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.core.Constants;
import service.core.BrokerService;

public class Server {
    public static void main(String[] args) {

        try{
            // Creating the RMI registry
            Registry registry = null;
            registry = LocateRegistry.createRegistry(1099);  

            // Instantiating a new broker service and passing in the RMI registry
            BrokerService broker = new LocalBrokerService(registry);

            // Exporting the stub for the broker service object
            BrokerService quotatoin_broker_service = (BrokerService) UnicastRemoteObject.exportObject(broker,0);

            // Registering the new objects with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, quotatoin_broker_service);

            // Displaying that the server is working
            System.out.println("STOPPING SERVER SHUTDOWN");

            // Ensuring the thread is asleep between connections
            while (true) {Thread.sleep(1000); }
            
        } catch (Exception e) {
            // Print error message if there is a problem
            System.out.println("Trouble: " + e);
        }
    }
}