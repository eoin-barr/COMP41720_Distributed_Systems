package service.dodgydrivers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception thrown when a quotation is not found
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchQuotationException extends RuntimeException {
    static final long serialVersionUID = -6516152229878843037L; 
}