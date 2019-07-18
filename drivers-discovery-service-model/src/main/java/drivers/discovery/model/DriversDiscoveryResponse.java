package drivers.discovery.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DriversDiscoveryResponse {
    public List<Driver> drivers;
}
