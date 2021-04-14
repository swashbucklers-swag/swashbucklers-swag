package com.sk8.swashbucklers.integration;

import com.sk8.swashbucklers.controller.InventoryController;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.repo.item.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.transaction.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class InventoryIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryController inventoryController;

    @Test
    void whenDefaultMapping_thenDirectoryDisplayed() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        Assertions.assertEquals("<h3>\n" +
                "  Supported Endpoints for /inventory:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/item-id :: GET</li>\n" +
                "  <li>/name :: GET</li>\n" +
                "  <li>/description :: GET</li>\n" +
                "  <li>/add :: POST</li>\n" +
                "  <li>/update/info :: PUT</li>\n" +
                "  <li>/update/quantity :: PUT</li>\n" +
                "  <li>/update/discount :: PUT</li>\n" +
                "  <li>/delete :: DELETE</li>\n" +
                "</ul>", result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenInventory_whenGetAll_thenInventoryRetrieved() throws Exception{
        Item item = new Item(0, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("Cool red boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(255.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(13))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenInventory_whenGetAllWithPagination_thenInventoryRetrievedWithPagination() throws Exception{
        Item item1 = new Item(0, "Boat1", "Cool red boat1", 255.99, 25);
        Item item2 = new Item(0, "Boat2", "Cool red boat2", 255.99, 25);
        Inventory inventory1 = new Inventory(0, item1, 5);
        item1.setInventory(inventory1);
        inventory1.setItem(item1);
        Inventory inventory2 = new Inventory(0, item2, 2);
        item2.setInventory(inventory2);
        inventory2.setItem(item2);
        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all?page=0&offset=50&sortby=\"quantity\"&order=\"DESC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Boat1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("Cool red boat1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(255.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(5))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].itemId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Boat2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].description").value("Cool red boat2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].price").value(255.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].discount").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].quantity").value(2))

                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenInventory_whenGetByItemId_thenInventoryRetrieved() throws Exception{
        Item item = new Item(0, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/item-id/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Cool red boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(255.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(13))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenInventory_whenGetByNameLike_thenInventoryRetrieved() throws Exception{
        Item item = new Item(0, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/name?text=boa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("Cool red boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(255.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(13))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenInventory_whenGetByDescriptionLike_thenInventoryRetrieved() throws Exception{
        Item item = new Item(0, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/description?text=red")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("Cool red boat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(255.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(13))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void addInventory_thenInventoryRetrieved() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Power Glove\", \"description\" : \"An all powerful gaming glove\", \"price\" : 1499.99, \"discount\" : 0, \"quantity\" : 15}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void addInventory_whenGetAll_thenInventoryRetrieved() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Power Glove\", \"description\" : \"An all powerful gaming glove\", \"price\" : 1499.99, \"discount\" : 0, \"quantity\" : 15}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void addInventory_whenGetAll_thenInventoryRetrieved_whenUpdateInfo_thenUpdatedInventoryRetrieved() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Power Glove\", \"description\" : \"An all powerful gaming glove\", \"price\" : 1499.99, \"discount\" : 0, \"quantity\" : 15}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.put("/inventory/update/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"itemId\" : 2, \"name\" : \"Power Glove 2.0\", \"description\" : \"Bigger and Better!\", \"price\" : 100.00}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove 2.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Bigger and Better!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove 2.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("Bigger and Better!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void addInventory_whenGetAll_thenInventoryRetrieved_whenUpdateQuantity_thenUpdatedInventoryRetrieved() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Power Glove\", \"description\" : \"An all powerful gaming glove\", \"price\" : 1499.99, \"discount\" : 0, \"quantity\" : 15}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.put("/inventory/update/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"itemId\" : 2,\"quantity\":255}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(255))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(255))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void addInventory_whenGetAll_thenInventoryRetrieved_whenUpdateDiscount_thenUpdatedInventoryRetrieved() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Power Glove\", \"description\" : \"An all powerful gaming glove\", \"price\" : 1499.99, \"discount\" : 0, \"quantity\" : 15}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.put("/inventory/update/discount")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"itemId\" : 2,\"discount\":50}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Transactional
    @Test
    void addInventory_whenGetAll_thenInventoryRetrieved_whenDeleteInventory_thenNoInventoryRetrieved() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Power Glove\", \"description\" : \"An all powerful gaming glove\", \"price\" : 1499.99, \"discount\" : 0, \"quantity\" : 15}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Power Glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("An all powerful gaming glove"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(1499.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(15))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.delete("/inventory/delete/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
