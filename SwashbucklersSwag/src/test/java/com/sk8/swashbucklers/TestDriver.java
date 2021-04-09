package com.sk8.swashbucklers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.order.Order;
import com.sk8.swashbucklers.model.order.OrderDetails;
import com.sk8.swashbucklers.model.order.StatusHistory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
class TestDriver {

	public static void main(String[] args) throws JsonProcessingException, ClassNotFoundException {
	}

	@Test
	void contextLoads() throws ClassNotFoundException {
		Customer c = new Customer(3, "bob", "smith", "bob@gmail.com", "passwordHash", "1315556611", new Location());
		Employee e = new Employee();
		Location l = new Location();
		StatusHistory s = new StatusHistory();
		ObjectMapper jsonMapper = new ObjectMapper();
		Set<OrderDetails> orderDetails = new HashSet<>();
		orderDetails.add(new OrderDetails());
		Item i = new Item();
		Inventory in = new Inventory();
		in.setItem(i);
		List<StatusHistory> h = new ArrayList<>();
		h.add(s);
		Order o = new Order(5, c, l, Timestamp.from(Instant.now()), h, orderDetails);
		try {
			System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o));
		} catch (Exception ignored){}
	}
}
