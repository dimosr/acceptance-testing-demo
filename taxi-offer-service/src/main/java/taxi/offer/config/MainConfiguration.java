package taxi.offer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import taxi.offer.business.Aggregator;
import taxi.offer.gateways.CustomerRatingServiceGateway;
import taxi.offer.gateways.DriverPreferenceServiceGateway;
import taxi.offer.gateways.DriversDiscoveryServiceGateway;
import taxi.offer.gateways.TripPricingServiceGateway;

@Configuration
public class MainConfiguration {

    @Autowired
    Environment environment;

    @Bean
    public TripPricingServiceGateway tripPricingServiceGateway() {
        int port = Integer.parseInt(environment.getProperty("trip-pricing-service.port"));
        return new TripPricingServiceGateway("localhost", port);
    }

    @Bean
    public CustomerRatingServiceGateway customerRatingServiceGateway() {
        int port = Integer.parseInt(environment.getProperty("customer-rating-service.port"));
        return new CustomerRatingServiceGateway("localhost", port);
    }

    @Bean
    public DriverPreferenceServiceGateway driverPreferenceServiceGateway() {
        int port = Integer.parseInt(environment.getProperty("driver-preference-service.port"));
        return new DriverPreferenceServiceGateway("localhost", port);
    }

    @Bean
    public DriversDiscoveryServiceGateway driversDiscoveryServiceGateway() {
        int port = Integer.parseInt(environment.getProperty("drivers-discovery-service.port"));
        return new DriversDiscoveryServiceGateway("localhost", port);
    }

    @Bean
    public Aggregator aggregator(final TripPricingServiceGateway tripPricingServiceGateway,
                                 final CustomerRatingServiceGateway customerRatingServiceGateway,
                                 final DriverPreferenceServiceGateway driverPreferenceServiceGateway,
                                 final DriversDiscoveryServiceGateway driversDiscoveryServiceGateway) {
        return new Aggregator(customerRatingServiceGateway, driverPreferenceServiceGateway, driversDiscoveryServiceGateway, tripPricingServiceGateway);
    }

}
