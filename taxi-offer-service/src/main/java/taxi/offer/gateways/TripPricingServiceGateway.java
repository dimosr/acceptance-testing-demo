package taxi.offer.gateways;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import trip.pricing.Trip;
import trip.pricing.TripPrice;

public class TripPricingServiceGateway {

    private static final String URL_FORMAT = "http://<host>:<port>/trip-price";

    private final String url;

    public TripPricingServiceGateway(final String host, final int port) {
        this.url = URL_FORMAT
                    .replace("<host>", host)
                    .replace("<port>", Integer.toString(port));
    }

    public TripPrice getPriceForTrip(final Trip trip) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Trip> request = new HttpEntity<>(trip);
        return restTemplate.postForObject(url, request, TripPrice.class);
    }
}
