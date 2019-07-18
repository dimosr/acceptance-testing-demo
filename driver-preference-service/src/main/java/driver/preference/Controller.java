package driver.preference;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController("/")
public class Controller {

    private Random randomGenerator = new Random();

    @ResponseBody
    @PostMapping(value = "/driver-preferences", consumes = "application/json", produces = "application/json")
    public DriverPreferencesResponse getDriverPreferences(@RequestBody DriverPreferenceRequest request) {
        return retrievePreferences(request);
    }

    /**
     * For simplicity, calculating random preferences
     */
    private DriverPreferencesResponse retrievePreferences(final DriverPreferenceRequest request) {
        Map<String, DriverPreferences> driverPreferencesMap = new HashMap<>();

        request.driverIds.forEach(driverId -> {
            int minCustomerRating = randomGenerator.nextInt(10) + 1;
            int randomValue = randomGenerator.nextInt(500) + 500;
            BigDecimal minTripPrice = BigDecimal.valueOf(randomValue).movePointLeft(2);

            DriverPreferences driverPreferences = new DriverPreferences(minCustomerRating, new TripPrice(minTripPrice, "USD"));

            driverPreferencesMap.put(driverId, driverPreferences);
        });

        return new DriverPreferencesResponse(driverPreferencesMap);
    }

}
