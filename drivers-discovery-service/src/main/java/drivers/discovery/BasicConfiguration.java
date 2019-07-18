package drivers.discovery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicConfiguration {

    @Bean
    public DriversLocator driversLocator() {
        return new DriversLocator();
    }

}
