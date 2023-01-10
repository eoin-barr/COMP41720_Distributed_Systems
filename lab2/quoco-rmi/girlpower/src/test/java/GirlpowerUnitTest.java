import org.junit.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.core.Constants;
import service.core.Quotation;
import service.core.ClientInfo;
import service.girlpower.GPQService;
import service.core.QuotationService;

public class GirlpowerUnitTest {

    // Declaring the registry variable
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService gpqService = new GPQService();
        try {
            // Creating a RMI registry
            registry = LocateRegistry.createRegistry(1099);

            // Export the stub for the Girlpower quotation service object
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(gpqService,0);

            // Register and label the quotation service with the RMI Registry
            registry.bind(Constants.GIRL_POWER_SERVICE, quotationService); 

        } catch (Exception e) {
            // Print error message if there is a problem
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        // Lookup and retrieve the stub of the Girlpower quotation service object
        QuotationService service = (QuotationService) registry.lookup(Constants.GIRL_POWER_SERVICE);

        // Assert that the service is not null
        assertNotNull(service);
    }

    @Test
    public void generateQuotationTest() throws Exception {
        // Lookup and retrieve the stub of the Girlpower quotation service object
        QuotationService service = (QuotationService) registry.lookup(Constants.GIRL_POWER_SERVICE);

        // Create a client info object
        ClientInfo info = new ClientInfo("Eoin", 'M', 23, 0, 0, "single");  
        
        // Generate a quotation  
        Quotation quote = service.generateQuotation(info);

        // Assert that the type of the quotation is correct
        assertTrue(quote.getClass().getName() == "service.core.Quotation" );
    }
} 