package taxi.offer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import taxi.offer.TaxiOfferRequest;
import taxi.offer.TaxiOfferResponse;
import taxi.offer.business.OfferService;

@RestController("/")
public class Controller {

    @Autowired
    private OfferService offerService;

    @ResponseBody
    @PostMapping(value = "/taxi-offers", consumes = "application/json", produces = "application/json")
    public TaxiOfferResponse getTaxiOffers(@RequestBody TaxiOfferRequest request) {
        return offerService.retrieveOffers(request);
    }

}
