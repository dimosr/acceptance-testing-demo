package customer.rating;

import customer.rating.model.CustomerId;
import customer.rating.model.CustomerRating;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController("/")
public class Controller {

    private Random randomGenerator = new Random();

    @ResponseBody
    @PostMapping(value = "/customer-rating", consumes = "application/json", produces = "application/json")
    public CustomerRating getCustomerRating(@RequestBody CustomerId customerId) {
        return calculateRating(customerId);
    }

    /**
     * For simplicity, returning a random rating.
     */
    private CustomerRating calculateRating(final CustomerId customerId) {
        int rating = randomGenerator.nextInt(10) + 1;

        return new CustomerRating(rating);
    }

}
