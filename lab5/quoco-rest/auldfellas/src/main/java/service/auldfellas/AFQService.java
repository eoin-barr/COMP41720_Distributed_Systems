package service.auldfellas;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
@RestController
public class AFQService extends AbstractQuotationService {
	// All references are to be prefixed with an AF (e.g. AF001000)
	public static final String PREFIX = "AF";
	public static final String COMPANY = "Auld Fellas Ltd.";

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
	 * 30% discount for being male
	 * 2% discount per year over 60
	 * 20% discount for less than 3 penalty points
	 * 50% penalty (i.e. reduction in discount) for more than 60 penalty points 
	 */
    private double price;
    private int discount;

    // Price getter method
    public double getPrice() {
        return this.price;
    }

    // Price setter method
    public void setPrice(double price) {
        this.price = price;
    }

    // Discount getter method
    public int getDiscount() {
        return this.discount;
    }

    // Discount setter method
    public void setDiscount(int discount) {
        this.discount = discount;
    }

	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 600 and 1200
		double price = generatePrice(600, 600);
        setPrice(price);
		
		// Automatic 30% discount for being male
		int discount = (info.getGender() == ClientInfo.MALE) ? 30:0;
		// Automatic 2% discount per year over 60...
		discount += (info.getAge() > 60) ? (2*(info.getAge()-60)) : 0;

		
		// Add a points discount
		discount += getPointsDiscount(info);
        setDiscount(discount);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (this.price * (100-this.discount)) / 100);
	}

	private int getPointsDiscount(ClientInfo info) {
        // 20% discount if less than 3 penalty points
		if (info.getPoints() < 3) return 20;

        // No discount if between 3 and 6 penalty points
		if (info.getPoints() <= 6) return 0;

        // 50% added if more than 6 penalty points
		return -50;
	}
}
