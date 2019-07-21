package drivers.discovery;

import drivers.discovery.model.Driver;
import drivers.discovery.model.DriversDiscoveryRequest;
import drivers.discovery.model.DriversDiscoveryResponse;
import drivers.discovery.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/")
public class Controller {

    @Autowired
    private DriversLocator driversLocator;

    @ResponseBody
    @PostMapping(value = "/drivers", consumes = "application/json", produces = "application/json")
    public DriversDiscoveryResponse getDrivers(@RequestBody DriversDiscoveryRequest request) {
        List<Driver> drivers = driversLocator.locateDrivers(request.getCurrentLocation());
        DriversDiscoveryResponse response = new DriversDiscoveryResponse(drivers);

        return response;
    }

}
