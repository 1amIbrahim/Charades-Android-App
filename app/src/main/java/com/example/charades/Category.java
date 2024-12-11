package com.example.charades;

import java.util.List;
import java.util.Map;

public class Category {
    private String name;
    private Map<Integer,String> items;

    public Category() {
        // Default constructor required for Firebase
    }

    public Category(String name, Map<Integer,String> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer,String> getItems() {
        return items;
    }

    public void setItems(Map<Integer,String> items) {
        this.items = items;
    }
}