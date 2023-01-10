package service.dodgydrivers; 

import service.core.Quotation;
import service.core.ClientInfo;
import service.core.AbstractQuotationService;

/**
 * Implementation of Quotation Service for Dodgy Drivers Insurance Company
 *  
 * @author Rem
 *
 */
public class DDQService extends AbstractQuotationService {
	// All references are to be prefixed with an DD (e.g. DD001000)
	public static final String PREFIX = "DD";
	public static final String COMPANY = "Dodgy Drivers Corp.";

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
