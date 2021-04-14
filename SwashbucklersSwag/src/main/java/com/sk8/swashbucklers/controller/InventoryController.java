package com.sk8.swashbucklers.controller;

import com.sk8.swashbucklers.dto.InventoryDTO;
import com.sk8.swashbucklers.dto.InventoryQuantityDTO;
import com.sk8.swashbucklers.dto.ItemDiscountDTO;
import com.sk8.swashbucklers.dto.ItemInfoDTO;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for inventory resource utilizing {@link InventoryService}
 * @author Daniel Bernier
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService INVENTORY_SERVICE;

    @Autowired
    public InventoryController(InventoryService inventoryService){
        this.INVENTORY_SERVICE = inventoryService;
    }


    /**
     * Default landing page for /inventory giving more information about requests and HTTP verbs
     * @return String with information supported endpoints
     */
    @GetMapping
    @PostMapping
    @PutMapping
    @DeleteMapping
    @RequestMapping
    @PatchMapping
    public String information(){
        return "<h3>\n" +
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
                "</ul>";
    }


    /**
     * Gets all inventory with pagination and sorting
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects with pagination and sorting applied
     */
    @GetMapping("/all")
    public Page<InventoryDTO> getAllInventory(
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "itemId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return INVENTORY_SERVICE.getAllInventory(page, offset, sortBy, order);
    }


    /**
     * Gets inventory who's item matches provided item id
     * @param id The item id of the inventory item
     * @return The data transfer representation of the requested Inventory
     */
    @GetMapping("/item-id/{id}")
    public InventoryDTO getInventoryByItemId(@PathVariable(name = "id") int id){
        return INVENTORY_SERVICE.getInventoryByItemId(id);
    }


    /**
     * Gets inventory items with names containing the given text, applies pagination and sorting
     * @param text The text being searched for in item names
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects who's names contain the given text with pagination and sorting applied
     */
    @GetMapping("/name")
    public Page<InventoryDTO> getAllInventoryWithNameLike(
            @RequestParam(value = "text") String text,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "itemId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return INVENTORY_SERVICE.getAllInventoryWithNameLike(text, page, offset, sortBy, order);
    }


    /**
     * Gets inventory items with descriptions containing the given text, applies pagination and sorting
     * @param text The text being searched for in item descriptions
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects who's descriptions contain the given text with pagination and sorting applied
     */
    @GetMapping("/description")
    public Page<InventoryDTO> getAllInventoryWithDescriptionLike(
            @RequestParam(value = "text") String text,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "itemId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return INVENTORY_SERVICE.getAllInventoryWithDescriptionLike(text, page, offset, sortBy, order);
    }


    /**
     * Adds a new inventory object
     * @param inventoryDTO The inventory to be added as a data transfer object
     * @return The data transfer representation of the newly added inventory object
     */
    @PostMapping("/add")
    public InventoryDTO addNewInventory(@RequestBody InventoryDTO inventoryDTO){
        inventoryDTO.setItemId(0);
        Inventory i = InventoryDTO.DTOToInventory().apply(inventoryDTO);
        return INVENTORY_SERVICE.createNewInventory(i);
    }


    /**
     * Landing page of update if no property was specified in uri
     * @return String informing user of correct update uri conventions
     */
    @PutMapping("/update")
    public String update(){
        return "Could not handle request, please use [/info | /quantity | /discount] to edit specific inventory attributes" +
                "\n\nExample:" +
                "\n/inventory/update/info" +
                "\n/inventory/update/quantity" +
                "\n/inventory/update/discount";
    }


    /**
     * Updates item information of inventory's item
     * @param itemInfoDTO The item info to be updated
     * @return The data transfer representation of the newly updated inventory
     */
    @PutMapping("/update/info")
    public InventoryDTO updateInventoryInfo(@RequestBody ItemInfoDTO itemInfoDTO){
        return INVENTORY_SERVICE.updateInventoryItemInfo(itemInfoDTO);
    }


    /**
     * Updates inventory's quantity field
     * @param inventoryQuantityDTO The inventory info to be updated
     * @return The data transfer representation of the newly updated inventory
     */
    @PutMapping("/update/quantity")
    public InventoryDTO updateInventoryInfo(@RequestBody InventoryQuantityDTO inventoryQuantityDTO){
        return INVENTORY_SERVICE.updateInventoryQuantity(inventoryQuantityDTO);
    }


    /**
     * Updates discount information of inventory's item
     * @param itemDiscountDTO The item info to be updated
     * @return The data transfer representation of the newly updated inventory
     */
    @PutMapping("/update/discount")
    public InventoryDTO updateInventoryInfo(@RequestBody ItemDiscountDTO itemDiscountDTO){
        return INVENTORY_SERVICE.updateItemDiscount(itemDiscountDTO);
    }


    /**
     * Deletes inventory object by provided item id
     * @param itemId The item id of the item who's inventory object will be deleted
     * @return String confirming deletion
     */
    @DeleteMapping("/delete/{id}")
    public String deleteInventoryByItemId(@PathVariable(name = "id") int itemId){
        INVENTORY_SERVICE.deleteInventoryByItemId(itemId);
        return "Deleted inventory item with id: " + itemId;
    }
}
