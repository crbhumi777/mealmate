package com.myproject.mealmate.Domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderItem {

    private String name;
    private int quantity;

    @Override
    public String toString() {
        return "{" + name + ":" + quantity + "}";
    }
}
