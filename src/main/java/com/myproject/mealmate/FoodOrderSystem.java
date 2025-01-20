package com.myproject.mealmate;

import com.myproject.mealmate.Domain.Order;
import com.myproject.mealmate.service.OrderService;
import com.myproject.mealmate.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class FoodOrderSystem {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    public void solve() {
        restaurantService.addOrUpdate("Italian mania", Map.of("king_burger", 10, "samosa_pizza", 20, "alu_pasta", 19), 15);
        restaurantService.addOrUpdate("Your Bite Stop", Map.of("bendi_macaroni", 12, "samosa_pizza", 25, "alu_pasta", 17), 12);

        restaurantService.updateMenu("Italian mania", Map.of("bendi_macaroni", 8, "king_burger", 15));
        System.out.println("All restaurants: " + restaurantService.getAll());

        Optional<Order> order1 = orderService.placeOrder(11, Map.of("bendi_macaroni", 3, "samosa_pizza", 2));
        System.out.println("Order placed: " + order1.get());
        System.out.println("All restaurants after order: " + restaurantService.getAll());

        System.out.println("All orders: " + orderService.getOrders());
        order1.ifPresent(order -> orderService.markOrderAsDelivered(order.getId()));
        System.out.println("All restaurants after delivery: " + restaurantService.getAll());
    }
}
