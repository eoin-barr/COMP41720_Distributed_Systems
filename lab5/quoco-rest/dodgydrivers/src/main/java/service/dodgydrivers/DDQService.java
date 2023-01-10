package service.dodgydrivers; 

import service.*;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Implementation of Quotation Service for Dodgy Drivers Insurance Company
 *  
 * @author Rem
 *
 */
@RestController
public class DDQService extends AbstractQuotationService {
	// All references are to be prefixed with an DD (e.g. DD001000)
	public static final String PREFIX = "DD";
	public static final String COMPANY = "Dodgy Drivers Corp.";

    // Declaring and initialising a map to store the quotations
    private Map<String, Quotation> quotations = new HashMap<>();

    // Creating a POST request mapping for the quotations endpoint
    @RequestMapping(value="/quotations",method=RequestMethod.POST)
    public ResponseEntity<Quotation> createQuotation(@RequestBody ClientInfo info) throws URISyntaxException {
        // Generating a quotation
        Quotation quotation = generateQuotation(info);
        // Adding the quotation to the quotattions map
        quotations.put(quotation.getReference(), quotation);
        // Creating a path for the quotation
        String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+ "/quotations/"+quotation.getReference(); 
        // Creating a header for the response     
        HttpHeaders headers = new HttpHeaders();
        // Setting the location as a new URI with the predefined path and adding it to the header
        headers.setLocation(new URI(path));
        // Returning a ResponseEntity which includes the quotation, the header and the status code
        return new ResponseEntity<>(quotation, headers, HttpStatus.CREATED);
    }

    // Creating a GET request mapping for the quotations/{reference} endpoint
    @RequestMapping(value="/quotations/{reference}",method=RequestMethod.GET) 
    public Quotation getResource(@PathVariable("reference") String reference) {
        // Getting the quotation from the quotations map
        Quotation quotation = quotations.get(reference);
        // If the quotation is null, throw a NoSuchQuotationException
        if (quotation == null) throw new NoSuchQuotationException(); 
        // Return the quotation
        return quotation;
    }
	
	/**
	 * Quote generation:
	 * 5% discount per penalty point (3 points required for qualification)
	 * 50% penalty for <= 3 penalty points
	 * 10% discount per year no claims
	 */

    private double price;
    private int discount;

    // Price getter method
    public double getPrice() {
        return this.price;
    }

    // Discount getter method
    public int getDiscount() {
        return this.discount;
    }

    // Price setter method
    public void setPrice(double price) {
        this.price = price;
    }

    // Discount setter method
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 800 and 1000
		double price = generatePrice(800, 200);
        setPrice(price);
		
		// 5% discount per penalty point (3 points required for qualification)
		int discount = (info.getPoints() > 3) ? 5*info.getPoints():-50;
		
		// Add a no claims discount
		discount += getNoClaimsDiscount(info);
        setDiscount(discount);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (this.price * (100-this.discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 10*info.getNoClaims();
	}

}
