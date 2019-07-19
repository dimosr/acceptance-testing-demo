package taxi.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import customer.rating.model.CustomerId;
import customer.rating.model.CustomerRating;
import driver.preference.DriverPreferenceRequest;
import driver.preference.DriverPreferencesResponse;
import drivers.discovery.model.DriversDiscoveryRequest;
import drivers.discovery.model.DriversDiscoveryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import taxi.offer.business.Aggregator;
import taxi.offer.config.AcceptanceTestConfig;
import taxi.offer.gateways.CustomerRatingServiceGateway;
import taxi.offer.gateways.DriverPreferenceServiceGateway;
import taxi.offer.gateways.DriversDiscoveryServiceGateway;
import taxi.offer.gateways.TripPricingServiceGateway;
import trip.pricing.Trip;
import trip.pricing.TripPrice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@Import(AcceptanceTestConfig.class)
@ActiveProfiles("acceptance-testing")
public class AcceptanceTest {

    @Autowired
    private Aggregator aggregator;

    @Autowired
    private CustomerRatingServiceGateway customerRatingServiceGateway;
    @Autowired
    private DriverPreferenceServiceGateway driverPreferenceServiceGateway;
    @Autowired
    private DriversDiscoveryServiceGateway driversDiscoveryServiceGateway;
    @Autowired
    private TripPricingServiceGateway tripPricingServiceGateway;

    @Before
    public void setup() throws IOException {
        getInputOutputPairs("dependencies", "customer-rating-service", CustomerId.class, CustomerRating.class).forEach((request, response) ->
                Mockito.when(customerRatingServiceGateway.findRating(request))
                    .thenReturn(response)
        );
        getInputOutputPairs("dependencies", "driver-preference-service", DriverPreferenceRequest.class, DriverPreferencesResponse.class).forEach((request, response) ->
                Mockito.when(driverPreferenceServiceGateway.findPreferences(request))
                        .thenReturn(response)
        );
        getInputOutputPairs("dependencies", "drivers-discovery-service", DriversDiscoveryRequest.class, DriversDiscoveryResponse.class).forEach((request, response) ->
                Mockito.when(driversDiscoveryServiceGateway.findDriversNearby(request))
                        .thenReturn(response)
        );
        getInputOutputPairs("dependencies", "trip-pricing-service", Trip.class, TripPrice.class).forEach((request, response) ->
                Mockito.when(tripPricingServiceGateway.getPriceForTrip(request))
                        .thenReturn(response)
        );
    }

    private <I, O> Map<I, O> getInputOutputPairs(final String directoryName, final String subDirectoryName, Class<I> inputClass, Class<O> outputClass) throws IOException {
        Path directoryPath = Paths.get("src", "test", "resources", directoryName, subDirectoryName);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<I, O> pairs = new HashMap<>();

        Files.walk(directoryPath)
                .filter(file -> file.getFileName().toAbsolutePath().toString().contains("input"))
                .forEach(inputFilePath -> {
                    String outputFileName = inputFilePath.toAbsolutePath().toString().replace("input", "output");
                    Path outputFilePath = Paths.get(outputFileName);

                    try {
                        String inputFileText = new String(Files.readAllBytes(inputFilePath), "UTF-8");
                        I input = objectMapper.readValue(inputFileText, inputClass);
                        String outputFileText = new String(Files.readAllBytes(outputFilePath), "UTF-8");
                        O output = objectMapper.readValue(outputFileText, outputClass);
                        pairs.put(input, output);
                    } catch (IOException e) {
                        throw new RuntimeException("failed to read file", e);
                    }

                });
        return pairs;
    }

    @Test
    public void test() throws IOException {
        getInputOutputPairs("requests", "taxi-offer-service", TaxiOfferRequest.class, TaxiOfferResponse.class).forEach((request, expectedResponse) -> {
                TaxiOfferResponse response = aggregator.retrieveOffers(request);
                assertThat(response).isEqualTo(expectedResponse);
        });
    }

}
