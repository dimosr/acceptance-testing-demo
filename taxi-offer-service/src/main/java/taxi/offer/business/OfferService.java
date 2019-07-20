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
import java.util.stream.Collectors;

public class OfferService {

    private OfferGenerator offerGenerator;

    private CustomerRatingServiceGateway customerRatingServiceGateway;
    private DriverPreferenceServiceGateway driverPreferenceServiceGateway;
    private DriversDiscoveryServiceGateway driversDiscoveryServiceGateway;
    private TripPricingServiceGateway tripPricingServiceGateway;

    public OfferService(final CustomerRatingServiceGateway customerRatingServiceGateway,
                        final DriverPreferenceServiceGateway driverPreferenceServiceGateway,
                        final DriversDiscoveryServiceGateway driversDiscoveryServiceGateway,
                        final TripPricingServiceGateway tripPricingServiceGateway,
                        final OfferGenerator offerGenerator) {
        this.customerRatingServiceGateway = customerRatingServiceGateway;
        this.driverPreferenceServiceGateway = driverPreferenceServiceGateway;
        this.driversDiscoveryServiceGateway = driversDiscoveryServiceGateway;
        this.tripPricingServiceGateway = tripPricingServiceGateway;
        this.offerGenerator = offerGenerator;
    }

    public TaxiOfferResponse retrieveOffers(final TaxiOfferRequest request) {
        String customerId = request.getCustomerId();
        Location startingPoint = request.getTrip().getStart();
        Location destination = request.getTrip().getEnd();

        final List<Driver> driversNearby = getDriversNearby(startingPoint);
        final CustomerRating customerRating = customerRatingServiceGateway.findRating(new CustomerId(customerId));
        final TripPrice tripPrice = getTripPrice(startingPoint, destination);
        final DriverPreferencesResponse driverPreferencesResponse = getDriverPreferencesResponse(driversNearby);

        final List<TripOffer> offers = offerGenerator.createOffers(driverPreferencesResponse.getDriverPreferences(), driversNearby, customerRating, tripPrice);
        return new TaxiOfferResponse(offers);
    }

    private DriverPreferencesResponse getDriverPreferencesResponse(List<Driver> driversNearby) {
        final List<String> nearbyDriverIds = driversNearby.stream().map(Driver::getId).collect(Collectors.toList());
        final DriverPreferenceRequest driverPreferenceRequest = new DriverPreferenceRequest(nearbyDriverIds);
        return driverPreferenceServiceGateway.findPreferences(driverPreferenceRequest);
    }

    private TripPrice getTripPrice(Location startingPoint, Location destination) {
        final trip.pricing.Location startLocation = new trip.pricing.Location(startingPoint.getLatitude(), startingPoint.getLongitude());
        final trip.pricing.Location endLocation = new trip.pricing.Location(destination.getLatitude(), destination.getLongitude());
        return tripPricingServiceGateway.getPriceForTrip(new Trip(startLocation, endLocation));
    }

    private List<Driver> getDriversNearby(Location startingPoint) {
        final drivers.discovery.model.Location location = new drivers.discovery.model.Location(startingPoint.getLongitude(), startingPoint.getLatitude());
        final DriversDiscoveryRequest driversDiscoveryRequest = new DriversDiscoveryRequest(location);
        return driversDiscoveryServiceGateway.findDriversNearby(driversDiscoveryRequest).getDrivers();
    }

}
