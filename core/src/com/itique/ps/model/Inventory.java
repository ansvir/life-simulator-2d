package com.itique.ps.model;

import java.util.List;
import java.util.UUID;

public class Inventory {
    private String id;
    private List<Item> items;

    public Inventory(List<Item> items) {
        this.id = UUID.randomUUID().toString();
        this.items = items;
    }

    public Inventory() {
        // for serialization
    }

    public String getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
