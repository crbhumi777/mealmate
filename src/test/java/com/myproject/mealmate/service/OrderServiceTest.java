package com.myproject.mealmate.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.myproject.mealmate.Domain.Order;
import com.myproject.mealmate.dao.OrdersRepo;
import com.myproject.mealmate.dto.Item;
import com.myproject.mealmate.dto.OrderStatus;
import com.myproject.mealmate.dto.Orders;
import com.myproject.mealmate.dto.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class OrderServiceTest {

    @Mock
    private OrdersRepo ordersRepo;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getOrders
    @Test
    void testGetOrders_returnsFormattedOrders() {
        // Arrange
        Orders mockOrder = new Orders();
        mockOrder.setId(1L);
        mockOrder.setUser_id(101L);
        com.myproject.mealmate.dto.OrderDetails detail = new com.myproject.mealmate.dto.OrderDetails();
        detail.setItemName("Pizza");
        detail.setRestaurantName("PizzaHut");
        detail.setQuantity(2);
        detail.setCost(20);
        mockOrder.setOrderDetails(List.of(detail));
        when(ordersRepo.findAll()).thenReturn(List.of(mockOrder));

        // Act
        List<Order> orders = orderService.getOrders();

        // Assert
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals("PizzaHut", orders.get(0).getOrderDetails().get(0).getRestaurantName());
        verify(ordersRepo, times(1)).findAll();
    }

    // Test for placeOrder (successful scenario)
    @Test
    void testPlaceOrder_returnsOrderOnSuccess() {
        // Arrange
        long customerId = 101L;
        Map<String, Integer> requestedItems = Map.of("Burger", 2);
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName("BurgerKing");

        Item mockItem = new Item();
        mockItem.setName("Burger");
        mockItem.setPrice(5);

        Orders mockOrders = new Orders();
        mockOrders.setId(1L);
        mockOrders.setUser_id(customerId);
        mockOrders.setOrderDetails(new ArrayList<>());

        when(restaurantService.getBestRestaurant("Burger", 2)).thenReturn(mockRestaurant);
        when(restaurantService.getItemByItemNameAndRestaurantName("Burger", mockRestaurant)).thenReturn(mockItem);
        when(ordersRepo.save(any(Orders.class))).thenReturn(mockOrders);

        // Act
        Optional<Order> order = orderService.placeOrder(customerId, requestedItems);

        // Assert
        assertTrue(order.isPresent());
        assertEquals(1L, order.get().getId());
        assertEquals(customerId, order.get().getUser());
        verify(restaurantService, times(1)).useCapacity(mockRestaurant, 2);
        verify(ordersRepo, times(1)).save(any(Orders.class));
    }

    // Test for markOrderAsDelivered
    @Test
    void testMarkOrderAsDelivered_updatesOrderStatusAndRestoresCapacity() {
        // Arrange
        Orders mockOrder = new Orders();
        mockOrder.setId(1L);
        mockOrder.setOrderStatus(OrderStatus.PLACED.toString());
        com.myproject.mealmate.dto.OrderDetails detail = new com.myproject.mealmate.dto.OrderDetails();
        detail.setItemName("Pizza");
        detail.setRestaurantName("PizzaHut");
        detail.setQuantity(2);
        mockOrder.setOrderDetails(List.of(detail));
        when(ordersRepo.findById(1L)).thenReturn(Optional.of(mockOrder));
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName("PizzaHut");

        when(restaurantService.getByName("PizzaHut")).thenReturn(mockRestaurant);

        // Act
        orderService.markOrderAsDelivered(1L);

        // Assert
        assertEquals(OrderStatus.DELIVERED.toString(), mockOrder.getOrderStatus());
        verify(restaurantService, times(1)).addCapacity(mockRestaurant, 2);
        verify(ordersRepo, times(1)).save(mockOrder);
    }

    // Test for markOrderAsDelivered (order not found scenario)
    @Test
    void testMarkOrderAsDelivered_orderNotFoundDoesNothing() {
        // Arrange
        when(ordersRepo.findById(1L)).thenReturn(Optional.empty());

        // Act
        orderService.markOrderAsDelivered(1L);

        // Assert
        verify(restaurantService, never()).addCapacity(any(), anyInt());
        verify(ordersRepo, never()).save(any());
    }
}
