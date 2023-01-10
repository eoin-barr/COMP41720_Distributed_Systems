package service.actor;

import service.auldfellas.*;

public class Init {
    // Declaring private variable service of type AFQService
    private AFQService service;

    // Constructor initialises the service variable
    public Init(AFQService instance) {
        this.service = instance;
    }

    // Getter for service
    public AFQService getQuotationService() {
        return this.service;
    }

    // Setter for service
    public void setQuotationService(AFQService instance) {
        this.service = instance;
    }
}
