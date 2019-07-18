package taxi.offer.gateways;

import drivers.discovery.model.DriversDiscoveryResponse;
import drivers.discovery.model.Location;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class DriversDiscoveryServiceGateway {

    private static final String URL_FORMAT = "http://<host>:<port>/drivers";

    private final String url;

    public DriversDiscoveryServiceGateway(final String host, final int port) {
        this.url = URL_FORMAT
                .replace("<host>", host)
                .replace("<port>", Integer.toString(port));
    }

    public DriversDiscoveryResponse findDriversNearby(final Location location) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Location> request = new HttpEntity<>(location);
        return restTemplate.postForObject(url, request, DriversDiscoveryResponse.class);
    }

}
