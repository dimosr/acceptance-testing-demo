package taxi.offer.gateways;

import customer.rating.model.CustomerId;
import customer.rating.model.CustomerRating;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class CustomerRatingServiceGateway {

    private static final String URL_FORMAT = "http://<host>:<port>/customer-rating";

    private final String url;

    public CustomerRatingServiceGateway(final String host, final int port) {
        this.url = URL_FORMAT
                .replace("<host>", host)
                .replace("<port>", Integer.toString(port));
    }

    public CustomerRating findRating(final CustomerId customerId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<CustomerId> request = new HttpEntity<>(customerId);
        return restTemplate.postForObject(url, request, CustomerRating.class);
    }

}
