package trip.pricing;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TripPrice {
    public BigDecimal price;
    public String currency;
}
