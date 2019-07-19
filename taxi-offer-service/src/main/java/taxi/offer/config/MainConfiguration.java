package taxi.offer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import taxi.offer.business.Aggregator;
import taxi.offer.gateways.CustomerRatingServiceGateway;
import taxi.offer.gateways.DriverPreferenceServiceGateway;
import taxi.offer.gateways.DriversDiscoveryServiceGateway;
import taxi.offer.gateways.TripPricingServiceGateway;

@Configuration
@Import(GatewaysConfiguration.class)
public class MainConfiguration {

    @Bean
    public Aggregator aggregator(final TripPricingServiceGateway tripPricingServiceGateway,
                                 final CustomerRatingServiceGateway customerRatingServiceGateway,
                                 final DriverPreferenceServiceGateway driverPreferenceServiceGateway,
                                 final DriversDiscoveryServiceGateway driversDiscoveryServiceGateway) {
        return new Aggregator(customerRatingServiceGateway, driverPreferenceServiceGateway, driversDiscoveryServiceGateway, tripPricingServiceGateway);
    }

}
