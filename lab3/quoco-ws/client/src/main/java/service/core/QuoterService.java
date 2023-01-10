package service.core;

import java.util.List;
import javax.jws.WebService;

// Interface to be implemented by the broker
@WebService
public interface QuoterService {
    public List<Quotation> getQuotations(ClientInfo info);
}
