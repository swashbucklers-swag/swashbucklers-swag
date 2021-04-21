package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.InventoryDTO;
import com.sk8.swashbucklers.dto.InventoryQuantityDTO;
import com.sk8.swashbucklers.dto.ItemDiscountDTO;
import com.sk8.swashbucklers.dto.ItemInfoDTO;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.repo.item.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Locale;
import java.util.Optional;

/**
 * This InventoryService allows for communication with {@link InventoryRepository} and enforces data constraints on requests to repository
 *
 * @author Daniel Bernier
 */
@Service
public class InventoryService {

    private final InventoryRepository INVENTORY_REPO;


    /**
     * Constructor to instantiate INVENTORY_REPO
     * @param inventoryRepo Inventory repository for use throughout the InventoryService
     */
    @Autowired
    public InventoryService(InventoryRepository inventoryRepo){
        this.INVENTORY_REPO = inventoryRepo;
    }


    /**
     * Gets all inventory items, applying pagination and sorting
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects with pagination and sorting applied
     */
    public Page<InventoryDTO> getAllInventory(int page, int offset, String sortBy, String order){

        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Inventory> inventories;
        if(order.equalsIgnoreCase("DESC"))
            inventories = INVENTORY_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            inventories = INVENTORY_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return inventories.map(InventoryDTO.inventoryToDTO());
    }


    /**
     * Persists an inventory object by calling {@link InventoryRepository#save(Object)} and returns the newly saved inventory object as its data transfer representation
     * @param inventory The Inventory object being persisted
     * @return The newly persisted inventory object converted to its data transfer representation using {@link InventoryDTO#inventoryToDTO()}
     */
    public InventoryDTO createNewInventory(Inventory inventory){
        Inventory saved = INVENTORY_REPO.save(inventory);
        return InventoryDTO.inventoryToDTO().apply(saved);
    }


    /**
     * Gets inventory item by item id using {@link InventoryRepository#findByItem_ItemId(int)}
     * @param itemId The item id of the inventory being requested
     * @return Data transfer object representation of Inventory object converted using {@link InventoryDTO#inventoryToDTO()}
     */
    public InventoryDTO getInventoryByItemId(int itemId){
        Optional<Inventory> requested = INVENTORY_REPO.findByItem_ItemId(itemId);
        return requested.map(inventory -> InventoryDTO.inventoryToDTO().apply(inventory)).orElse(null);
    }


    /**
     * Gets inventory items with names containing the given text, applies pagination and sorting
     * @param text The text being searched for in item name
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects who's names contain the given text with pagination and sorting applied
     */
    public Page<InventoryDTO> getAllInventoryWithNameLike(String text, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Inventory> inventories;
        if(order.equalsIgnoreCase("DESC"))
            inventories = INVENTORY_REPO.findByItem_NameContainingIgnoreCase(text, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            inventories = INVENTORY_REPO.findByItem_NameContainingIgnoreCase(text, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return inventories.map(InventoryDTO.inventoryToDTO());
    }


    /**
     * Gets inventory items with descriptions containing the given text, applies pagination and sorting
     * @param text The text being searched for in item descriptions
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects who's descriptions contain the given text with pagination and sorting applied
     */
    public Page<InventoryDTO> getAllInventoryWithDescriptionLike(String text, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Inventory> inventories;
        if(order.equalsIgnoreCase("DESC"))
            inventories = INVENTORY_REPO.findByItem_DescriptionContainingIgnoreCase(text, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            inventories = INVENTORY_REPO.findByItem_DescriptionContainingIgnoreCase(text, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return inventories.map(InventoryDTO.inventoryToDTO());
    }


    /**
     * Updates inventory item's info
     * @param itemInfoDTO Item info to be updated
     * @return Data transfer representation of updated object
     */
    public InventoryDTO updateInventoryItemInfo(ItemInfoDTO itemInfoDTO){
        Optional<Inventory> inventoryOptional = INVENTORY_REPO.findByItem_ItemId(itemInfoDTO.getItemId());

        if(!inventoryOptional.isPresent())
            return null;

        //updating fields
        Inventory inventory = inventoryOptional.get();
        inventory.getItem().setName(itemInfoDTO.getName());
        inventory.getItem().setDescription(itemInfoDTO.getDescription());
        inventory.getItem().setPrice(itemInfoDTO.getPrice());

        return InventoryDTO.inventoryToDTO().apply(INVENTORY_REPO.save(inventory));
    }


    /**
     * Updates inventory quantity info
     * @param inventoryQuantityDTO inventory info being updated
     * @return Data transfer representation of updated object
     */
    public InventoryDTO updateInventoryQuantity(InventoryQuantityDTO inventoryQuantityDTO){
        Optional<Inventory> inventoryOptional = INVENTORY_REPO.findByItem_ItemId(inventoryQuantityDTO.getItemId());

        if(!inventoryOptional.isPresent())
            return null;

        //updating fields
        Inventory inventory = inventoryOptional.get();
        inventory.setQuantity(inventoryQuantityDTO.getQuantity());

        return InventoryDTO.inventoryToDTO().apply(INVENTORY_REPO.save(inventory));
    }


    /**
     * Updates item discount information
     * @param itemDiscountDTO Item discount information to update
     * @return Data transfer representation of updated object
     */
    public InventoryDTO updateItemDiscount(ItemDiscountDTO itemDiscountDTO){
        Optional<Inventory> inventoryOptional = INVENTORY_REPO.findByItem_ItemId(itemDiscountDTO.getItemId());

        if(!inventoryOptional.isPresent())
            return null;

        //updating fields
        Inventory inventory = inventoryOptional.get();
        inventory.getItem().setDiscount(itemDiscountDTO.getDiscount());

        return InventoryDTO.inventoryToDTO().apply(INVENTORY_REPO.save(inventory));
    }


    /**
     * Deletes Inventory object based on inventory's item's id
     * @param itemId The item id of the item associated with the inventory object to be deleted
     */
    public void deleteInventoryByItemId(int itemId){
        INVENTORY_REPO.deleteByItem_ItemId(itemId);
    }


    /**
     * Ensures permitted page format
     * @param page The page number value being validated
     * @return A valid page number value
     */
    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }


    /**
     * Ensures permitted offset format
     * @param offset The offset value being validated
     * @return A valid offset value
     */
    private int validateOffset(int offset){
        if(offset != 5 && offset != 10 && offset != 25 && offset != 50 && offset != 100)
            offset = 25;
        return offset;
    }


    /**
     * Ensures permitted sortby format
     * @param sortBy The sortby value being validated
     * @return A valid sortby value
     */
    private String validateSortBy(String sortBy){
        switch (sortBy.toLowerCase(Locale.ROOT)){
            case "quantity":
                return "quantity";
            case "name":
                return "item.name";
            case "description":
                return "item.description";
            case "price":
                return "item.price";
            case "discount":
                return "item.discount";
            default:
                return "item.itemId";
        }
    }
}
