package taxi.offer.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import taxi.offer.gateways.CustomerRatingServiceGateway;
import taxi.offer.gateways.DriverPreferenceServiceGateway;
import taxi.offer.gateways.DriversDiscoveryServiceGateway;
import taxi.offer.gateways.TripPricingServiceGateway;

@Configuration
@Profile("acceptance-testing")
public class StubGatewaysConfig {

    @Bean
    public TripPricingServiceGateway tripPricingServiceGateway() {
        return Mockito.mock(TripPricingServiceGateway.class);
    }

    @Bean
    public CustomerRatingServiceGateway customerRatingServiceGateway() {
        return Mockito.mock(CustomerRatingServiceGateway.class);
    }

    @Bean
    public DriverPreferenceServiceGateway driverPreferenceServiceGateway() {
        return Mockito.mock(DriverPreferenceServiceGateway.class);
    }

    @Bean
    public DriversDiscoveryServiceGateway driversDiscoveryServiceGateway() {
        return Mockito.mock(DriversDiscoveryServiceGateway.class);
    }

}
