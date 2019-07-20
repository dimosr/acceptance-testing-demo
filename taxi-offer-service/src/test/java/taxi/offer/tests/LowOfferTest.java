package taxi.offer.tests;

import org.junit.Test;
import taxi.offer.util.BaseAcceptanceTest;

import java.io.IOException;

/**
 * Testing that offers with low price are filtered out.
 */
public class LowOfferTest extends BaseAcceptanceTest {

    @Test
    public void testLowOffersAreFilteredOut() throws IOException {
        executeTestFromFolder("low-offers");
    }

}
