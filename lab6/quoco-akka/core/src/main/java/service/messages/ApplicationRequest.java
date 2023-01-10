package service.messages;

import service.core.ClientInfo;

public class ApplicationRequest implements MyInterface {

    private ClientInfo clientInfo;
    private int applicationId;

    public ApplicationRequest(int id, ClientInfo clientInfo) {
        this.applicationId = id;
        this.clientInfo = clientInfo;
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public int getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(int id) {
        this.applicationId = id;
    }
}
