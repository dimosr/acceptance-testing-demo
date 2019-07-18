package driver.preference;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DriverPreferences {
    public int minimumCustomerRating;
    public TripPrice minimumTripPrice;
}
