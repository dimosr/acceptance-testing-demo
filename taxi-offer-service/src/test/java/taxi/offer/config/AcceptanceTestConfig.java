package taxi.offer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MainConfiguration.class,
        StubGatewaysConfig.class
})
public class AcceptanceTestConfig {}
