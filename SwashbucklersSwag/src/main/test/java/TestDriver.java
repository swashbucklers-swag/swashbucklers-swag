import com.SwashbucklersSwag.model.customer.Customer;
import com.SwashbucklersSwag.model.location.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDriver {

    public static void main(String[] args) throws JsonProcessingException {
        Customer c = new Customer(3, "bob", "smith", "bob@gmail.com", "passwordHash", "1315556611", new Location());
        ObjectMapper jsonMapper = new ObjectMapper();
        System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(c));
    }
}
