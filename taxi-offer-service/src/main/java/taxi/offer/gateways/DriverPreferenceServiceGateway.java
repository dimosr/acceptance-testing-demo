package taxi.offer.gateways;

import driver.preference.DriverPreferenceRequest;
import driver.preference.DriverPreferencesResponse;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class DriverPreferenceServiceGateway {

    private static final String URL_FORMAT = "http://<host>:<port>/driver-preferences";

    private final String url;

    public DriverPreferenceServiceGateway(final String host, final int port) {
        this.url = URL_FORMAT
                .replace("<host>", host)
                .replace("<port>", Integer.toString(port));
    }

    public DriverPreferencesResponse findPreferences(final DriverPreferenceRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<DriverPreferenceRequest> httpRequest = new HttpEntity<>(request);
        return restTemplate.postForObject(url, httpRequest, DriverPreferencesResponse.class);
    }

}
