package com.myproject.mealmate.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Order {

    private Long id;
    private long user;
    private List<OrderDetails> orderDetails;

    @Override
    public String toString() {
        return String.format("{'order_id': '%d', 'user': '%d', 'order_details': %s}", id, user, orderDetails);
    }
}
