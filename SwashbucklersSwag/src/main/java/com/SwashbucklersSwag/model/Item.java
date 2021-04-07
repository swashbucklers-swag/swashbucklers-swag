package com.SwashbucklersSwag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private int itemId;
    @Column
    private String description;
    @Column
    private String name;
    @Column
    private int quantity;
    //@Column
    //private double price;


}
