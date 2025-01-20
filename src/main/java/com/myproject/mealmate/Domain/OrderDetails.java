package com.myproject.mealmate.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class OrderDetails {

    private String restaurantName;
    private List<OrderItem> items;
    private int cost;

    @Override
    public String toString() {
        return String.format("{'restaurant': '%s', 'items': '%s', 'cost': %s}", restaurantName, items, cost);
    }

}
