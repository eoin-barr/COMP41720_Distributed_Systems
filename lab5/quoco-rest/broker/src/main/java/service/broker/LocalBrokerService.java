package service.broker;

import service.*;
import java.net.*;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class LocalBrokerService {

    // Declaring and initialising a map to store the applications
    static HashMap<Long, ClientApplication> application_cache = new HashMap<>();

    // Retrieving the value of the service urls from the application.properties file
    @Value("#{'${server.broker_urls}'.split(',\\s*')}")
    List<String> broker_urls;

    public ClientApplication getQuotations(ClientApplication app) {
        // Initialising a placeholder application as null
        ClientApplication placeholder_client_application = null;
        // Creating a new RestTemplate
        RestTemplate rest_template = new RestTemplate();
        // Creating a new HttpEntity quotation request object
        HttpEntity<ClientApplication>  quotation_req = new HttpEntity<>(app);
        // Iterating through the service urls
        for (String url : broker_urls) {
            // Checking if the cache does not contain the application Id
            if (!application_cache.containsKey(app.getClientApplicationId())) {
                // If it does not, add the application to the cache
                application_cache.put(app.getClientApplicationId(), new ClientApplication(app.getClientInfo(), app.getClientApplicationId()));
            }
            // Sending the request to the server and storing the response as a Quotation object
            Quotation quotation = rest_template.postForObject(url +"quotations", quotation_req, Quotation.class);
            // Checking if the cache contains an application with the id of the current application
            if (application_cache.get(app.getClientApplicationId()) != null) {
                // If it does, get the application from the cache
                placeholder_client_application = application_cache.get(app.getClientApplicationId());
                // Add the quotation to the application map
                placeholder_client_application.getQuotations().add(quotation);
            }
        }
        return placeholder_client_application;
    }

    // Creating a POST request mapping for the applications endpoint
    @RequestMapping(value="/applications",method=RequestMethod.POST)
    public ResponseEntity<ClientApplication> postQuotation(@RequestBody ClientApplication app) {
        try {
            ClientApplication client_application = getQuotations(app);
            // Creating a path for the applications
            String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/applications/";
            // Creating headers for the response
            HttpHeaders headers = new HttpHeaders();
            try {
                // Setting the location as a new URI with the predefined path and adding it to the header
                headers.setLocation(new URI(path));
                // Returning a ResponseEntity which includes the application, the header and the status code
                return new ResponseEntity<>(client_application, headers, HttpStatus.CREATED);
            } catch (URISyntaxException e) {
                // Handle any exceptions
                System.out.println("Error: " + e);
            }
        }catch (Exception e) {
            // Handle any exceptions
            System.out.println("Error: " + e);
        }
        // Return null if there is an error
        return null;
    }

    public long parseClientApplicationId(String id) {
        // Parsing the client application id to a long
        long application_id = Long.parseLong(id);
        return application_id;
    }

    // Creating a GET request mapping for the applications/{id} endpoint
    @RequestMapping(value="/applications/{id}",method=RequestMethod.GET)
    public ResponseEntity<ClientApplication> getApplicationById(@PathVariable("id") String client_application_id) {
        try {
            // Initialising retrieved application variable as null
            ClientApplication retrieved_client_application = null;
            // Parsing the client application id to a long
            long new_id = parseClientApplicationId(client_application_id);
            // Checking if the cache does not contain an application with the id of the current application
            if (!application_cache.containsKey(new_id)) {
                // If it does not, return null
                return null;
            }
            // If it does, get the application from the cache
            retrieved_client_application = application_cache.get(new_id);
            // Creating a path for the quotation
            String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/quotation/";
            // Creating headers for the response
            HttpHeaders headers = new HttpHeaders();
            try {
                // Setting the location as a new URI with the predefined path and adding it to the header
                headers.setLocation(new URI(path));
                // Returning a ResponseEntity which includes the application, the header and the status code
                return new ResponseEntity<>(retrieved_client_application, headers, HttpStatus.CREATED);
            }catch (Exception e) {
                // Handle any errors
                System.out.println(e);
            }
        }catch (Exception e) {
            // Handle any exceptions
            System.out.println("Error: " + e);
        }
        // Return null if there is an error
        return null;
    }

    // Creating a GET request mapping for the applications endpoint
    @RequestMapping(value="/applications", method=RequestMethod.GET)
    public ResponseEntity<List<ClientApplication>> getAllApplications() {
        try {
            // Creating a new list of applications
            List<ClientApplication> client_applications = new ArrayList<ClientApplication>();
            // Iterating through the cache
            for (ClientApplication temp_application : application_cache.values()) {
                // Adding the applications in the cache to the list
                client_applications.add(temp_application);
            }
            // Creating a path for the quotation
            String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/quotations/";
            // Creating headers for the response
            HttpHeaders headers = new HttpHeaders();
            try {
                // Setting the location as a new URI with the predefined path and adding it to the header
                headers.setLocation(new URI(path));
                // Returning a ResponseEntity which includes the application, the header and the status code
                return new ResponseEntity<>(client_applications, headers, HttpStatus.CREATED);
            } catch (URISyntaxException e) {
                // Handle any errors
                System.out.println(e);
            }
        }catch (Exception e) {
            // Handle any errors
            System.out.println(e);
        }
        // Return null if there are any errors
        return null;
    }
}
