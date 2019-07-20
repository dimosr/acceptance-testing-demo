package taxi.offer.tests;

import org.junit.Test;
import taxi.offer.util.BaseAcceptanceTest;

import java.io.IOException;


public class SimpleAcceptanceTest extends BaseAcceptanceTest {

    @Test
    public void simpleTest() throws IOException {
        executeTestFromFolder("simple-test");
    }

}
