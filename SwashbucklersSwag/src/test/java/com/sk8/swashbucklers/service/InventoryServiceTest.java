package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.repo.item.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class InventoryServiceTest {

    @MockBean
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;


}
