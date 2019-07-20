package taxi.offer.business;

import customer.rating.model.CustomerRating;
import driver.preference.DriverPreferences;
import drivers.discovery.model.Driver;
import taxi.offer.TripOffer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OfferGenerator {

    public List<TripOffer> createOffers(final Map<String, DriverPreferences> driverPreferences,
                                        final List<Driver> availableDrivers,
                                        final CustomerRating customerRating,
                                        final trip.pricing.TripPrice tripPrice) {
        final Set<String> filteredDriverIds = driverPreferences.entrySet().stream().filter(entry -> {
            boolean customerRatingThresholdSatisfied = entry.getValue().getMinimumCustomerRating() < customerRating.getRating();
            boolean sameCurrency = entry.getValue().getMinimumTripPrice().getCurrency().equals(tripPrice.getCurrency());
            boolean minimumPriceThresholdSatisfied = entry.getValue().getMinimumTripPrice().getPrice().compareTo(tripPrice.getPrice()) < 0;

            return customerRatingThresholdSatisfied && sameCurrency && minimumPriceThresholdSatisfied;
        }).map(Map.Entry::getKey).collect(Collectors.toSet());

        return availableDrivers.stream()
                .filter(driver -> filteredDriverIds.contains(driver.getId()))
                .map(driver -> new TripOffer(new taxi.offer.TripPrice(tripPrice.getPrice(), tripPrice.getCurrency()), driver.getFirstName() + " " + driver.getLastName()))
                .collect(Collectors.toList());
    }

}
