package drivers.discovery;

import drivers.discovery.model.Driver;
import drivers.discovery.model.Location;

import java.util.Arrays;
import java.util.List;

class DriversLocator {

    /**
     * For simplicity, returning a static list of drivers regardless of location.
     */
    private List<Driver> drivers = Arrays.asList(
            new Driver("ASDFASDR", "George", "Callum", 24),
            new Driver("qWEQWEZX", "Alan", "Kay", 35),
            new Driver("XVPQWKSS", "Nick", "Callaghan", 32),
            new Driver("PQWEIASD", "Adam", "Dillan", 44),
            new Driver("VCPWIWEZ", "Bob", "Drake", 57)
    );

    List<Driver> locateDrivers(final Location location) {
        return drivers;
    }
}
