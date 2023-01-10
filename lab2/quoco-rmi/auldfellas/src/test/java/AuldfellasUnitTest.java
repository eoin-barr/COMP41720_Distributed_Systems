import org.junit.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.core.Constants;
import service.core.Quotation;
import service.core.ClientInfo;
import service.core.QuotationService;
import service.auldfellas.AFQService;

public class AuldfellasUnitTest {

    // Declaring the registry variable
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService afqService = new AFQService();
        try {
            // Creating a RMI registry
            registry = LocateRegistry.createRegistry(1099);

            // Export the stub for the Auldfellas quotation service object
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,0);

            // Register and label the quotation service with the RMI Registry
            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);

        } catch (Exception e) {
            // Print error message if there is a problem
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        // Lookup and retrieve the stub of the Auldfellas quotation service object
        QuotationService service = (QuotationService) registry.lookup(Constants.AULD_FELLAS_SERVICE);

        // Assert that the service is not null
        assertNotNull(service);
    }

    @Test
    public void generateQuotationTest() throws Exception {
        // Lookup and retrieve the stub of the Auldfellas quotation service object
        QuotationService service = (QuotationService) registry.lookup(Constants.AULD_FELLAS_SERVICE);

        // Create a client info object
        ClientInfo info = new ClientInfo("Eoin", 'M', 23, 0, 0, "single");

        // Generate a quotation
        Quotation quote = service.generateQuotation(info);

        // Assert that the type of the quotation is correct
        assertTrue(quote.getClass().getName() == "service.core.Quotation" );
    }
} 