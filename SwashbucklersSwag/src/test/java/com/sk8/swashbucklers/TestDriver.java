package com.sk8.swashbucklers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.repo.item.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;

@SpringBootTest
class TestDriver {

	public static void main(String[] args) throws JsonProcessingException, ClassNotFoundException {
	}

	@Test
	@Transactional
	void contextLoads(@Autowired InventoryRepository inrepo) throws ClassNotFoundException {
		//------------Testing JSON formatting----------------
//		Customer c = new Customer(3, "bob", "smith", "bob@gmail.com", "passwordHash", "1315556611", new Location());
//		Employee e = new Employee();
//		Location l = new Location();
//		StatusHistory s = new StatusHistory();
//		ObjectMapper jsonMapper = new ObjectMapper();
//		Set<OrderDetails> orderDetails = new HashSet<>();
//		orderDetails.add(new OrderDetails());
		//----------------testing inventory repo---------------------
		Item i = new Item(0, "cool boat", "red boat", 155.50, 0);
		Inventory in = new Inventory();
		i.setInventory(in);
		in.setItem(i);
		in.setQuantity(26);
		inrepo.save(in);
		System.out.println("\nall");
		System.out.println(inrepo.findAll());
		System.out.println("\ndescription");
		System.out.println(inrepo.findByItem_DescriptionContainingIgnoreCase("cool", PageRequest.of(0,25)).getContent());
		System.out.println("\nitem id");
		in = inrepo.findByItem_ItemId(2).get();
		System.out.println(in);
		System.out.println("\nname");
		System.out.println(inrepo.findByItem_NameContainingIgnoreCase("red", PageRequest.of(0,25)).getContent());
		System.out.println("\nall");
		System.out.println(inrepo.findAll());
		System.out.println("\nupdate");
		in.setQuantity(666);
		in.getItem().setDiscount(75);
		inrepo.save(in);
		System.out.println(inrepo.findAll());
//		System.out.println("delete");
//		inrepo.deleteByItem_ItemId(2);
//		System.out.println("\nall");
//		System.out.println(inrepo.findAll());

	}
}
