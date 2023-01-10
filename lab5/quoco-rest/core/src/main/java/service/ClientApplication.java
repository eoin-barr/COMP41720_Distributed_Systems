package service;

import java.util.ArrayList;

public class ClientApplication {
    private ClientInfo client_info;
    private long client_application_id;
    private ArrayList<Quotation> quotations;

    public ClientInfo getClientInfo() {
        return this.client_info;
    }

    public void setClientInfo(ClientInfo info) {
        this.client_info = info;
    }

    public long getClientApplicationId() {
        return this.client_application_id;
    }

    public void setClientApplicationId(long id) {
        this.client_application_id = id;
    }

    public ArrayList<Quotation> getQuotations() {
        return this.quotations;
    }

    public void setQuotations(ArrayList<Quotation> qs) {
        this.quotations = qs;
    }

    public void addQuotation(Quotation q) {
        this.quotations.add(q);
    }

    public ClientApplication(ClientInfo info, long id) {
        this.client_info = info;
        this.client_application_id = id;
        this.quotations = new ArrayList<Quotation>();
    }

    public ClientApplication(){}
}
