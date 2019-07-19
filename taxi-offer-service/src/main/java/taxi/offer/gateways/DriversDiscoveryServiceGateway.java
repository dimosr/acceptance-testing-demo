package taxi.offer.gateways;

import drivers.discovery.model.DriversDiscoveryRequest;
import drivers.discovery.model.DriversDiscoveryResponse;
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

    public DriversDiscoveryResponse findDriversNearby(DriversDiscoveryRequest driversDiscoveryRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<DriversDiscoveryRequest> request = new HttpEntity<>(driversDiscoveryRequest);
        return restTemplate.postForObject(url, request, DriversDiscoveryResponse.class);
    }

}
