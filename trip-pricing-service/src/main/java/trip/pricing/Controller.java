package trip.pricing;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Random;

@RestController("/")
public class Controller {

    private Random randomGenerator = new Random();

    @ResponseBody
    @PostMapping(value = "/trip-price", consumes = "application/json", produces = "application/json")
    public TripPrice getTripPrice(@RequestBody Trip trip) {
        return calculatePrice(trip);
    }

    /**
     * For simplicity, returning a random price.
     */
    private TripPrice calculatePrice(final Trip trip) {
        int randomValue = randomGenerator.nextInt(500) + 500;
        BigDecimal price = BigDecimal.valueOf(randomValue).movePointLeft(2);

        return new TripPrice(price, "USD");
    }

}
