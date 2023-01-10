package service.messages;

import service.core.Quotation;

public class QuotationResponse implements MyInterface {
    private int id;
    private Quotation quotation;

    public QuotationResponse(int id, Quotation quotation) {
    this.id = id;
    this.quotation = quotation;
    }
    // add get and set methods for each field
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Quotation getQuotation() {
        return this.quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }
}