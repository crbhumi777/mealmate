package com.myproject.mealmate.service;

import com.myproject.mealmate.Domain.Order;
import com.myproject.mealmate.Domain.OrderDetails;
import com.myproject.mealmate.Domain.OrderItem;
import com.myproject.mealmate.dao.OrdersRepo;
import com.myproject.mealmate.dto.Item;
import com.myproject.mealmate.dto.OrderStatus;
import com.myproject.mealmate.dto.Orders;
import com.myproject.mealmate.dto.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrdersRepo ordersRepo;

    @Autowired
    private RestaurantService restaurantService;

    public List<Order> getOrders() {
        List<Orders> orders = (List<Orders>) ordersRepo.findAll();
        List<Order> result = new ArrayList<>();
        for (Orders order : orders) {
            Order o = new Order(order.getId(), order.getUser_id(), new ArrayList<>());
            for (com.myproject.mealmate.dto.OrderDetails od : order.getOrderDetails()) {
                if (o.getOrderDetails().stream().anyMatch(orderDetails -> orderDetails.getRestaurantName().equals(od.getRestaurantName()))) {
                    o.getOrderDetails().stream().findFirst().ifPresent(orderDetails -> {
                        orderDetails.getItems().add(new OrderItem(od.getItemName(), od.getQuantity()));
                        orderDetails.setCost(orderDetails.getCost() + od.getCost());
                    });
                } else {
                    o.getOrderDetails().add(new OrderDetails(od.getRestaurantName(),
                            Collections.singletonList(new OrderItem(od.getItemName(), od.getQuantity())),
                            od.getCost()));
                }
            }
            result.add(o);
        }
        return result;
    }

    public Optional<Order> placeOrder(long customer, Map<String, Integer> requestedItems) {
        Map<String, Integer> remainingItems = new HashMap<>(requestedItems);
        Orders orders = new Orders();
        orders.setOrderStatus(OrderStatus.PLACED.toString());
        orders.setUser_id(customer);
        List<com.myproject.mealmate.dto.OrderDetails> orderDetails = new ArrayList<>();
        for (String item : requestedItems.keySet()) {
            int quantity = requestedItems.get(item);
            Restaurant availableRestaurant = restaurantService.getBestRestaurant(item, quantity);
            if (availableRestaurant == null) {
                break;
            }
            Item i = restaurantService.getItemByItemNameAndRestaurantName(item, availableRestaurant);
            com.myproject.mealmate.dto.OrderDetails od = new com.myproject.mealmate.dto.OrderDetails();
            od.setItemName(item);
            od.setQuantity(quantity);
            od.setRestaurantName(availableRestaurant.getName());
            od.setCost(i.getPrice() * quantity);
            od.setOrders(orders);
            orderDetails.add(od);
            restaurantService.useCapacity(availableRestaurant, quantity);
            remainingItems.remove(item);
        }
        orders.setOrderDetails(orderDetails);
        orders = ordersRepo.save(orders);

        if (remainingItems.isEmpty()) {
            Order o = new Order(orders.getId(), orders.getUser_id(), new ArrayList<>());
            for (com.myproject.mealmate.dto.OrderDetails od : orders.getOrderDetails()) {
                if (o.getOrderDetails().stream().anyMatch(ods -> ods.getRestaurantName().equals(od.getRestaurantName()))) {
                    o.getOrderDetails().stream().findFirst().ifPresent(ods -> {
                        ods.getItems().add(new OrderItem(od.getItemName(), od.getQuantity()));
                        ods.setCost(ods.getCost() + od.getCost());
                    });
                } else {
                    o.getOrderDetails().add(new OrderDetails(od.getRestaurantName(),
                            Collections.singletonList(new OrderItem(od.getItemName(), od.getQuantity())),
                            od.getCost()));
                }
            }
            return Optional.of(o);
        } else {
            return Optional.empty();
        }
    }

    public void markOrderAsDelivered(long orderId) {
        Orders orders = ordersRepo.findById(orderId).orElse(null);
        if (orders != null) {
            for (com.myproject.mealmate.dto.OrderDetails orderDetails : orders.getOrderDetails()) {
                Restaurant restaurant = restaurantService.getByName(orderDetails.getRestaurantName());
                restaurantService.addCapacity(restaurant, orderDetails.getQuantity());
            }
            orders.setOrderStatus(OrderStatus.DELIVERED.toString());
            ordersRepo.save(orders);
        }
    }
}
