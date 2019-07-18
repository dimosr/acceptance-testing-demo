package taxi.offer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TaxiOfferRequest {
    public Trip trip;
    public String customerId;
}
