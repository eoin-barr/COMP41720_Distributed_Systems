package service.broker;

import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import service.core.Quotation;
import service.core.ClientInfo;
import service.core.BrokerService;
import service.core.QuotationService;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService, Serializable {

    // Declaring the registry variable
    Registry service_registry;

    // Constructor for the LocalBrokerService
    public LocalBrokerService(Registry service_registry_init) {
        this.service_registry = service_registry_init;
    }

    @Override
	public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {
		List<Quotation> quotations = new LinkedList<Quotation>();

            // Iterating through the list of services
			for (String name : service_registry.list()) {
				// Searching for Quotation Companys' services and adding the Quotations to the list
				if (name.startsWith("qs-")) {
                    try {
                        QuotationService service = (QuotationService) service_registry.lookup(name);
					    quotations.add(service.generateQuotation(info));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
				}
			}

		return quotations;
	}

    @Override
    public void registerService(String name, java.rmi.Remote service) throws RemoteException {
        try {
            // Registry binds the service passed to the function
            this.service_registry.bind(name, service);
            
        } catch (Exception e) {
            // Print error message if there is a problem
            e.printStackTrace(); 
        }
    }

}