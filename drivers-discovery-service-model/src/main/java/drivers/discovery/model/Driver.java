package drivers.discovery.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Driver {
    public String ID;
    public String firstName;
    public String lastName;
    public int age;
}
