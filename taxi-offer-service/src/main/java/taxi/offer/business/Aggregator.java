package taxi.offer.business;

import customer.rating.model.CustomerId;
import customer.rating.model.CustomerRating;
import driver.preference.DriverPreferenceRequest;
import driver.preference.DriverPreferencesResponse;
import drivers.discovery.model.Driver;
import drivers.discovery.model.DriversDiscoveryRequest;
import taxi.offer.Location;
import taxi.offer.TaxiOfferRequest;
import taxi.offer.TaxiOfferResponse;
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

    public TaxiOfferResponse retrieveOffers(final TaxiOfferRequest request) {
        String customerId = request.getCustomerId();
        Location startingPoint = request.getTrip().getStart();
        Location destination = request.getTrip().getEnd();

        final drivers.discovery.model.Location location = new drivers.discovery.model.Location(startingPoint.getLongitude(), startingPoint.getLatitude());
        final DriversDiscoveryRequest driversDiscoveryRequest = new DriversDiscoveryRequest(location);
        final List<Driver> driversNearby = driversDiscoveryServiceGateway.findDriversNearby(driversDiscoveryRequest).getDrivers();

        final CustomerRating customerRating = customerRatingServiceGateway.findRating(new CustomerId(customerId));

        final trip.pricing.Location startLocation = new trip.pricing.Location(startingPoint.getLatitude(), startingPoint.getLongitude());
        final trip.pricing.Location endLocation = new trip.pricing.Location(destination.getLatitude(), destination.getLongitude());
        final TripPrice tripPrice = tripPricingServiceGateway.getPriceForTrip(new Trip(startLocation, endLocation));

        final List<String> nearbyDriverIds = driversNearby.stream().map(driver -> driver.getId()).collect(Collectors.toList());
        final DriverPreferenceRequest driverPreferenceRequest = new DriverPreferenceRequest(nearbyDriverIds);
        final DriverPreferencesResponse driverPreferencesResponse = driverPreferenceServiceGateway.findPreferences(driverPreferenceRequest);

        final Set<String> filteredDriverIds = driverPreferencesResponse.getDriverPreferences().entrySet().stream().filter(entry -> {
            boolean customerRatingThresholdSatisfied = entry.getValue().getMinimumCustomerRating() < customerRating.getRating();
            boolean sameCurrency = entry.getValue().getMinimumTripPrice().getCurrency().equals(tripPrice.getCurrency());
            boolean minimumPriceThresholdSatisfied = entry.getValue().getMinimumTripPrice().getPrice().compareTo(tripPrice.getPrice()) < 0;

            return customerRatingThresholdSatisfied && sameCurrency && minimumPriceThresholdSatisfied;
        }).map(Map.Entry::getKey).collect(Collectors.toSet());

        List<TripOffer> offers = driversNearby.stream()
                .filter(driver -> filteredDriverIds.contains(driver.getId()))
                .map(driver -> new TripOffer(new taxi.offer.TripPrice(tripPrice.getPrice(), tripPrice.getCurrency()), driver.getFirstName() + " " + driver.getLastName()))
                .collect(Collectors.toList());

        return new TaxiOfferResponse(offers);
    }

}
