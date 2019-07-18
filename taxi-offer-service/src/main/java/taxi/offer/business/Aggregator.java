package taxi.offer.business;

import customer.rating.model.CustomerId;
import customer.rating.model.CustomerRating;
import driver.preference.DriverPreferenceRequest;
import driver.preference.DriverPreferencesResponse;
import drivers.discovery.model.Driver;
import taxi.offer.Location;
import taxi.offer.TripOffer;
import taxi.offer.gateways.CustomerRatingServiceGateway;
import taxi.offer.gateways.DriverPreferenceServiceGateway;
import taxi.offer.gateways.DriversDiscoveryServiceGateway;
import taxi.offer.gateways.TripPricingServiceGateway;
import trip.pricing.Trip;
import trip.pricing.TripPrice;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Aggregator {

    private CustomerRatingServiceGateway customerRatingServiceGateway;
    private DriverPreferenceServiceGateway driverPreferenceServiceGateway;
    private DriversDiscoveryServiceGateway driversDiscoveryServiceGateway;
    private TripPricingServiceGateway tripPricingServiceGateway;

    public Aggregator(final CustomerRatingServiceGateway customerRatingServiceGateway,
                      final DriverPreferenceServiceGateway driverPreferenceServiceGateway,
                      final DriversDiscoveryServiceGateway driversDiscoveryServiceGateway,
                      final TripPricingServiceGateway tripPricingServiceGateway) {
        this.customerRatingServiceGateway = customerRatingServiceGateway;
        this.driverPreferenceServiceGateway = driverPreferenceServiceGateway;
        this.driversDiscoveryServiceGateway = driversDiscoveryServiceGateway;
        this.tripPricingServiceGateway = tripPricingServiceGateway;
    }

    public List<TripOffer> retrieveOffers(final Location startingPoint, final Location destination, final String customerId) {
        final drivers.discovery.model.Location location = new drivers.discovery.model.Location(startingPoint.longitude, startingPoint.latitude);
        final List<Driver> driversNearby = driversDiscoveryServiceGateway.findDriversNearby(location).drivers;

        final CustomerRating customerRating = customerRatingServiceGateway.findRating(new CustomerId(customerId));

        final trip.pricing.Location startLocation = new trip.pricing.Location(startingPoint.latitude, startingPoint.longitude);
        final trip.pricing.Location endLocation = new trip.pricing.Location(destination.latitude, destination.longitude);
        final TripPrice tripPrice = tripPricingServiceGateway.getPriceForTrip(new Trip(startLocation, endLocation));

        final List<String> nearbyDriverIds = driversNearby.stream().map(driver -> driver.ID).collect(Collectors.toList());
        final DriverPreferenceRequest driverPreferenceRequest = new DriverPreferenceRequest(nearbyDriverIds);
        final DriverPreferencesResponse driverPreferencesResponse = driverPreferenceServiceGateway.findPreferences(driverPreferenceRequest);

        final Set<String> filteredDriverIds = driverPreferencesResponse.driverPreferences.entrySet().stream().filter(entry -> {
            boolean customerRatingThresholdSatisfied = entry.getValue().minimumCustomerRating < customerRating.rating;
            boolean sameCurrency = entry.getValue().minimumTripPrice.currency.equals(tripPrice.currency);
            boolean minimumPriceThresholdSatisfied = entry.getValue().minimumTripPrice.price.compareTo(tripPrice.price) < 0;

            return customerRatingThresholdSatisfied && sameCurrency && minimumPriceThresholdSatisfied;
        }).map(Map.Entry::getKey).collect(Collectors.toSet());

        return driversNearby.stream()
                .filter(driver -> filteredDriverIds.contains(driver.ID))
                .map(driver -> new TripOffer(new taxi.offer.TripPrice(tripPrice.price, tripPrice.currency),driver.ID))
                .collect(Collectors.toList());
    }

}