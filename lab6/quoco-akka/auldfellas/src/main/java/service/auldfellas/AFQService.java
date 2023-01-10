package service.auldfellas;

import service.core.Quotation;
import service.core.ClientInfo;
import service.core.AbstractQuotationService;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
public class AFQService extends AbstractQuotationService {
	// All references are to be prefixed with an AF (e.g. AF001000)
	public static final String PREFIX = "AF";
	public static final String COMPANY = "Auld Fellas Ltd.";
	
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
