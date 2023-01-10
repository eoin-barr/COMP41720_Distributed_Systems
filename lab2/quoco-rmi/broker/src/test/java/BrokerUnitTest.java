import java.util.List;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.core.Constants;
import service.core.Quotation;
import service.core.ClientInfo;
import service.core.BrokerService;

import service.broker.LocalBrokerService;

public class BrokerUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        try {
            // Creating a RMI registry
            registry = LocateRegistry.createRegistry(1099);

            // Instantiating a new broker service and passing in the RMI registry
            BrokerService broker = new LocalBrokerService(registry);

            // Export the stub for the broker service service object
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(broker,0);

            // Register and label the quotation service with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE,brokerService);

        } catch (Exception e) {
            // Print error message if there is a problem
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        // Lookup and retrieve the stub of the broker service object
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

        // Assert that the service is not null
        assertNotNull(service);
    }

    @Test
    public void generateQuotationTest() throws RemoteException {
        // Create a client info object
        ClientInfo info = new ClientInfo("Eoin",'M',23,3,20,"qwerfgyu");
        Registry registry = LocateRegistry.getRegistry(1099);
        try {
            // Lookup and retrieve the stub of the broker service object
            BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

            // Add quotations to the quotationList
            List<Quotation> quotationList = brokerService.getQuotations(info);

            // Assert that the quotationList is empty
            assertTrue(quotationList.isEmpty());
            
        }catch (Exception e) {
            // Print error message if there is a problem
            System.out.println("Trouble: " + e);
        }
    }
}