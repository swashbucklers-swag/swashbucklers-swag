package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.*;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.repo.item.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * tests for {@link InventoryService}
 *
 * @author Daniel Bernier
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:test-application.properties")
class InventoryServiceTest {

    @MockBean
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

    @Test
    void whenGetAllInventory_returnsPageOfInventoryDTO(){
        List<Inventory> inventoryArrayList = new ArrayList<>();
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryArrayList.add(inventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryArrayList);

        Mockito.when(inventoryRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(inventoryPage);

        Page<InventoryDTO> response = inventoryService.getAllInventory(0, 5, "itemId", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getItemId());
        Assertions.assertEquals("Boat", response.getContent().get(0).getName());
        Assertions.assertEquals("Cool red boat", response.getContent().get(0).getDescription());
        Assertions.assertEquals(255.99, response.getContent().get(0).getPrice());
        Assertions.assertEquals(25, response.getContent().get(0).getDiscount());
        Assertions.assertEquals(13, response.getContent().get(0).getQuantity());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());

        response = inventoryService.getAllInventory(0, 10, "itemId", "ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getItemId());
        Assertions.assertEquals("Boat", response.getContent().get(0).getName());
        Assertions.assertEquals("Cool red boat", response.getContent().get(0).getDescription());
        Assertions.assertEquals(255.99, response.getContent().get(0).getPrice());
        Assertions.assertEquals(25, response.getContent().get(0).getDiscount());
        Assertions.assertEquals(13, response.getContent().get(0).getQuantity());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());
    }

    @Test
    void whenCreateNewInventory_returnsNewlyCreatedInventoryDTO(){
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);

        Mockito.when(inventoryRepository.save(org.mockito.ArgumentMatchers.isA(Inventory.class))).thenReturn(inventory);

        InventoryDTO response = inventoryService.createNewInventory(inventory);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getItemId());
        Assertions.assertEquals("Boat", response.getName());
        Assertions.assertEquals("Cool red boat", response.getDescription());
        Assertions.assertEquals(255.99, response.getPrice());
        Assertions.assertEquals(25, response.getDiscount());
        Assertions.assertEquals(13, response.getQuantity());
        System.out.println(response);
    }

    @Test
    void whenGetByItemID_returnsInventoryDTO(){
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);

        Mockito.when(inventoryRepository.findByItem_ItemId(1)).thenReturn(Optional.of(inventory));
        Mockito.when(inventoryRepository.findByItem_ItemId(255)).thenReturn(Optional.empty());

        InventoryDTO response = inventoryService.getInventoryByItemId(1);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getItemId());
        Assertions.assertEquals("Boat", response.getName());
        Assertions.assertEquals("Cool red boat", response.getDescription());
        Assertions.assertEquals(255.99, response.getPrice());
        Assertions.assertEquals(25, response.getDiscount());
        Assertions.assertEquals(13, response.getQuantity());
        System.out.println(response);

        response = inventoryService.getInventoryByItemId(255);
        Assertions.assertNull(response);
        System.out.println(response);
    }

    @Test
    void whenGetAllInventoryWithNameLike_returnsPageOfInventoryDTO(){
        List<Inventory> inventoryArrayList = new ArrayList<>();
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryArrayList.add(inventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryArrayList);

        Mockito.when(inventoryRepository.findByItem_NameContainingIgnoreCase(org.mockito.ArgumentMatchers.isA(String.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(inventoryPage);

        Page<InventoryDTO> response = inventoryService.getAllInventoryWithNameLike("Boa", 0, 25, "quantity", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getItemId());
        Assertions.assertEquals("Boat", response.getContent().get(0).getName());
        Assertions.assertEquals("Cool red boat", response.getContent().get(0).getDescription());
        Assertions.assertEquals(255.99, response.getContent().get(0).getPrice());
        Assertions.assertEquals(25, response.getContent().get(0).getDiscount());
        Assertions.assertEquals(13, response.getContent().get(0).getQuantity());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());

        response = inventoryService.getAllInventoryWithNameLike("Boa", 0, 50, "name", "ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getItemId());
        Assertions.assertEquals("Boat", response.getContent().get(0).getName());
        Assertions.assertEquals("Cool red boat", response.getContent().get(0).getDescription());
        Assertions.assertEquals(255.99, response.getContent().get(0).getPrice());
        Assertions.assertEquals(25, response.getContent().get(0).getDiscount());
        Assertions.assertEquals(13, response.getContent().get(0).getQuantity());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());
    }

    @Test
    void whenGetAllInventoryWithDescriptionLike_returnsPageOfInventoryDTO(){
        List<Inventory> inventoryArrayList = new ArrayList<>();
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryArrayList.add(inventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryArrayList);

        Mockito.when(inventoryRepository.findByItem_DescriptionContainingIgnoreCase(org.mockito.ArgumentMatchers.isA(String.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(inventoryPage);

        Page<InventoryDTO> response = inventoryService.getAllInventoryWithDescriptionLike("red", 0, 100, "price", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getItemId());
        Assertions.assertEquals("Boat", response.getContent().get(0).getName());
        Assertions.assertEquals("Cool red boat", response.getContent().get(0).getDescription());
        Assertions.assertEquals(255.99, response.getContent().get(0).getPrice());
        Assertions.assertEquals(25, response.getContent().get(0).getDiscount());
        Assertions.assertEquals(13, response.getContent().get(0).getQuantity());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());

        response = inventoryService.getAllInventoryWithDescriptionLike("red", 0, 5, "discount", "ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getItemId());
        Assertions.assertEquals("Boat", response.getContent().get(0).getName());
        Assertions.assertEquals("Cool red boat", response.getContent().get(0).getDescription());
        Assertions.assertEquals(255.99, response.getContent().get(0).getPrice());
        Assertions.assertEquals(25, response.getContent().get(0).getDiscount());
        Assertions.assertEquals(13, response.getContent().get(0).getQuantity());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());
    }

    @Test
    void whenUpdateInventoryItemInfo_returnsInventoryDTO(){
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);

        Mockito.when(inventoryRepository.findByItem_ItemId(1)).thenReturn(Optional.of(inventory));
        Mockito.when(inventoryRepository.save(org.mockito.ArgumentMatchers.isA(Inventory.class))).thenReturn(inventory);

        InventoryDTO response = inventoryService.updateInventoryItemInfo(new ItemInfoDTO(1, "Boat", "Cool red boat", 255.99));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getItemId());
        Assertions.assertEquals("Boat", response.getName());
        Assertions.assertEquals("Cool red boat", response.getDescription());
        Assertions.assertEquals(255.99, response.getPrice());
        Assertions.assertEquals(25, response.getDiscount());
        Assertions.assertEquals(13, response.getQuantity());
        System.out.println(response);
    }

    @Test
    void whenUpdateInventoryQuantity_returnsInventoryDTO(){
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);

        Mockito.when(inventoryRepository.findByItem_ItemId(1)).thenReturn(Optional.of(inventory));
        Mockito.when(inventoryRepository.save(org.mockito.ArgumentMatchers.isA(Inventory.class))).thenReturn(inventory);

        InventoryDTO response = inventoryService.updateInventoryQuantity(new InventoryQuantityDTO(1, 13));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getItemId());
        Assertions.assertEquals("Boat", response.getName());
        Assertions.assertEquals("Cool red boat", response.getDescription());
        Assertions.assertEquals(255.99, response.getPrice());
        Assertions.assertEquals(25, response.getDiscount());
        Assertions.assertEquals(13, response.getQuantity());
        System.out.println(response);
    }

    @Test
    void whenUpdateInventoryItemDiscount_returnsInventoryDTO(){
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);

        Mockito.when(inventoryRepository.findByItem_ItemId(1)).thenReturn(Optional.of(inventory));
        Mockito.when(inventoryRepository.save(org.mockito.ArgumentMatchers.isA(Inventory.class))).thenReturn(inventory);

        InventoryDTO response = inventoryService.updateItemDiscount(new ItemDiscountDTO(1, 25));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getItemId());
        Assertions.assertEquals("Boat", response.getName());
        Assertions.assertEquals("Cool red boat", response.getDescription());
        Assertions.assertEquals(255.99, response.getPrice());
        Assertions.assertEquals(25, response.getDiscount());
        Assertions.assertEquals(13, response.getQuantity());
        System.out.println(response);
    }

    @Test
    void whenDeleteInventoryByItemId_callsDeleteByItem_ItemId(){
        inventoryService.deleteInventoryByItemId(1);
        Mockito.verify(inventoryRepository, Mockito.times(1)).deleteByItem_ItemId(1);
    }
}
