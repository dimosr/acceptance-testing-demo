package taxi.offer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import taxi.offer.business.OfferService;
import taxi.offer.business.OfferGenerator;
import taxi.offer.gateways.CustomerRatingServiceGateway;
import taxi.offer.gateways.DriverPreferenceServiceGateway;
import taxi.offer.gateways.DriversDiscoveryServiceGateway;
import taxi.offer.gateways.TripPricingServiceGateway;

@Configuration
@Import(GatewaysConfiguration.class)
public class MainConfiguration {

    @Bean
    public OfferService aggregator(final TripPricingServiceGateway tripPricingServiceGateway,
                                   final CustomerRatingServiceGateway customerRatingServiceGateway,
                                   final DriverPreferenceServiceGateway driverPreferenceServiceGateway,
                                   final DriversDiscoveryServiceGateway driversDiscoveryServiceGateway,
                                   final OfferGenerator offerGenerator) {
        return new OfferService(customerRatingServiceGateway, driverPreferenceServiceGateway, driversDiscoveryServiceGateway, tripPricingServiceGateway, offerGenerator);
    }

    @Bean
    public OfferGenerator offerGenerator() {
        return new OfferGenerator();
    }

}
