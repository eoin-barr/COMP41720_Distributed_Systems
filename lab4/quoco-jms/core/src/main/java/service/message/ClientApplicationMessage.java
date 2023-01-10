package service.message;

import java.io.Serializable;
import java.util.ArrayList;

import service.core.Quotation;
import service.core.ClientInfo;

public class ClientApplicationMessage implements Serializable {
    // Declaring public client_info variable with type ClientInfo
    public ClientInfo client_info;

    // Declaring public quotations variable with type ArrayList
    public ArrayList<Quotation> quotations;

    // Declaring public client_app_message_id variable with type long
    public long client_app_message_id;

    public ClientApplicationMessage(long received_client_app_message_id , ClientInfo received_client_info ){
        // Assigning a new ArrayList to the quotations variable
        this.quotations = new ArrayList<>();

        // Assigning the received client info to the client info variable
        this.client_info = received_client_info;

        // Assigning the received client application message id to the client_app_message_id variable
        this.client_app_message_id = received_client_app_message_id;
    }
}