package service.actor;

import service.dodgydrivers.*;

public class Init {
    // Declaring private variable service of type DDQService
    private DDQService service;
    
    // Constructor initialises the service variable
    public Init(DDQService instance) {
        this.service = instance;
    }

    // Getter for service
    public DDQService getQuotationService() {
        return this.service;
    }

    // Setter for service
    public void setQuotationService(DDQService instance) {
        this.service = instance;
    }
}
