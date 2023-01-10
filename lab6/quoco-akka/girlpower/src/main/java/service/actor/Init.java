package service.actor;

import service.girlpower.*;

public class Init {
    // Declaring private variable service of type GPQService
    private GPQService service;

    // Constructor initialises the service variable    
    public Init(GPQService instance) {
        this.service = instance;
    }

    // Getter for service
    public GPQService getQuotationService() {
        return this.service;
    }

    // Setter for service
    public void setQuotationService(GPQService instance) {
        this.service = instance;
    }
}
