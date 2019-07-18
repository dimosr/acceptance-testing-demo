package driver.preference;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Map;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DriverPreferencesResponse {
    public Map<String, DriverPreferences> driverPreferences;
}
