package drivers.discovery.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DriversDiscoveryRequest {
    public Location currentLocation;
}
