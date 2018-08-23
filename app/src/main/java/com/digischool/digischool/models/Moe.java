package com.digischool.digischool.models;

public class Moe {
    private String item, quantity;

    public Moe() {
    }

    public Moe(String item, String quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
