package driver.preference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DriverPreferences {
    private int minimumCustomerRating;
    private TripPrice minimumTripPrice;
}
