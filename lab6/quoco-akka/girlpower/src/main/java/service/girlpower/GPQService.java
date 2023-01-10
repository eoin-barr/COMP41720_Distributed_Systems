package service.girlpower;

import service.core.Quotation;
import service.core.ClientInfo;
import service.core.AbstractQuotationService;

public class GPQService extends AbstractQuotationService  {
    // All references are to be prefixed with an DD (e.g. DD001000)
	public static final String PREFIX = "GP";
	public static final String COMPANY = "Girl Power Inc.";

    /**
	 * Quote generation:
	 * 50% discount for being female
	 * 20% discount for no penalty points
	 * 15% discount for < 3 penalty points
	 * no discount for 3-5 penalty points
	 * 100% penalty for > 5 penalty points
	 * 5% discount per year no claims
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
		// Create an initial quotation between 600 and 1000
		double price = generatePrice(600, 400);
		setPrice(price);
		// Automatic 50% discount for being female
		int discount = (info.getGender() == ClientInfo.FEMALE) ? 50:0;
		
		// Add a points discount
		discount += getPointsDiscount(info);
		
		// Add a no claims discount
		discount += getNoClaimsDiscount(info);
		setDiscount(discount);
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (this.price * (100-this.discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 5*info.getNoClaims();
	}

	private int getPointsDiscount(ClientInfo info) {
        // 20% discount if no penalty points
		if (info.getPoints() == 0) return 20;

        // 15% discount if less than 3 penalty points
		if (info.getPoints() < 3) return 15;

        // No discount if between 3 and 6 penalty points
		if (info.getPoints() < 6) return 0;

        // 100% added if more than 6 penalty points
		return -100;
	}
}
