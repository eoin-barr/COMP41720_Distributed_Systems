package service;

/**
 * Class to store the quotations returned by the quotation services
 * 
 * @author Rem
 *
 */
public class Quotation implements java.io.Serializable {
    private String reference;
    private String company;
    private double price;

	public Quotation(String company, String reference, double price)  {
		this.company = company;
		this.reference = reference;
		this.price = price;
	}
	
    public  Quotation() {}

    public String getReference() {
        return this.reference;
    }

    public void setReference(String ref){
        this.reference = ref;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
