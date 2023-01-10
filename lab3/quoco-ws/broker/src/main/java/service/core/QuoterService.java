package service.core;

import javax.jws.WebMethod;
import javax.jws.WebService;

// Interface to be implemented by the quotation services
@WebService
public interface QuoterService {
    @WebMethod
	Quotation generateQuotation(ClientInfo info);
}