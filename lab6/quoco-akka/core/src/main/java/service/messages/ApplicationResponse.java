package service.messages;

import java.util.List;
import java.util.ArrayList;

import service.core.ClientInfo;
import service.core.Quotation;

public class ApplicationResponse implements MyInterface {
    private int applicationId;
    private ClientInfo clientInfo;
    private List<Quotation> quotations;

    public ApplicationResponse(int id, ClientInfo info) {
        this.applicationId = id;
        this.clientInfo = info;
        this.quotations = new ArrayList<>();
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public void setClientInfo(ClientInfo info) {
        this.clientInfo = info;
    }

    public List<Quotation> getQuotations() {
        return this.quotations;
    }
    
    public void addQuotation(Quotation quotation) {
        this.quotations.add(quotation);
    } 

    public void setQuotations(List<Quotation> qs) {
        this.quotations = qs;
    }

    public int getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(int id) {
        this.applicationId = id;
    }
    
}
