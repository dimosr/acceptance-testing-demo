package taxi.offer.tests;

import org.junit.Test;
import taxi.offer.util.BaseAcceptanceTest;

import java.io.IOException;

/**
 * Testing that customers that drivers are removed from offers, when customer have low rating.
 */
public class LowCustomerRatingTest extends BaseAcceptanceTest {

    @Test
    public void testCustomersWithLowRatingAreRejected() throws IOException {
        executeTestFromFolder("low-customer-rating");
    }

}
