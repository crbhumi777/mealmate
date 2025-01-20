package com.myproject.mealmate.service;

import com.myproject.mealmate.dao.ItemRepo;
import com.myproject.mealmate.dao.RestaurantRepo;
import com.myproject.mealmate.dto.Item;
import com.myproject.mealmate.dto.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for addOrUpdate
    @Test
    void testAddOrUpdate_shouldSaveRestaurantAndMenuItems() {
        // Arrange
        String restaurantName = "Test Restaurant";
        Map<String, Integer> menu = Map.of("Pizza", 10, "Burger", 15);
        int capacity = 50;

        // Act
        restaurantService.addOrUpdate(restaurantName, menu, capacity);

        // Assert
        verify(restaurantRepo, times(1)).save(any(Restaurant.class));
    }

    // Test for getAll
    @Test
    void testGetAll_shouldReturnAllRestaurants() {
        // Arrange
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Restaurant 1");
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Restaurant 2");
        when(restaurantRepo.findAll()).thenReturn(List.of(restaurant1, restaurant2));

        // Act
        List<Restaurant> restaurants = restaurantService.getAll();

        // Assert
        assertNotNull(restaurants);
        assertEquals(2, restaurants.size());
        verify(restaurantRepo, times(1)).findAll();
    }

    // Test for getByName
    @Test
    void testGetByName_shouldReturnRestaurantByName() {
        // Arrange
        String restaurantName = "Test Restaurant";
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName(restaurantName);
        when(restaurantRepo.findByName(restaurantName)).thenReturn(mockRestaurant);

        // Act
        Restaurant restaurant = restaurantService.getByName(restaurantName);

        // Assert
        assertNotNull(restaurant);
        assertEquals(restaurantName, restaurant.getName());
        verify(restaurantRepo, times(1)).findByName(restaurantName);
    }

    // Test for getItemByItemNameAndRestaurantName
    @Test
    void testGetItemByItemNameAndRestaurantName_shouldReturnItem() {
        // Arrange
        String itemName = "Pizza";
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName("Test Restaurant");
        Item mockItem = new Item();
        mockItem.setName(itemName);
        mockItem.setPrice(10);
        when(itemRepo.findByNameAndRestaurant(itemName, mockRestaurant)).thenReturn(mockItem);

        // Act
        Item item = restaurantService.getItemByItemNameAndRestaurantName(itemName, mockRestaurant);

        // Assert
        assertNotNull(item);
        assertEquals(itemName, item.getName());
        verify(itemRepo, times(1)).findByNameAndRestaurant(itemName, mockRestaurant);
    }

    // Test for updateMenu
    @Test
    void testUpdateMenu_shouldUpdateMenuPrices() {
        // Arrange
        String restaurantName = "Test Restaurant";
        Map<String, Integer> menu = Map.of("Pizza", 12);
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName(restaurantName);
        Item mockItem = new Item();
        mockItem.setName("Pizza");
        mockItem.setPrice(10);
        when(restaurantRepo.findByName(restaurantName)).thenReturn(mockRestaurant);
        when(itemRepo.findByNameAndRestaurant("Pizza", mockRestaurant)).thenReturn(mockItem);

        // Act
        restaurantService.updateMenu(restaurantName, menu);

        // Assert
        verify(itemRepo, times(1)).save(mockItem);
        assertEquals(12, mockItem.getPrice());
    }

    // Test for getBestRestaurant
    @Test
    void testGetBestRestaurant_shouldReturnSuitableRestaurant() {
        // Arrange
        String itemName = "Pizza";
        int quantity = 5;
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName("Best Restaurant");
        when(restaurantRepo.findSuitableRestaurantByMenuItemPrice(itemName, quantity))
                .thenReturn(Optional.of(mockRestaurant));

        // Act
        Restaurant restaurant = restaurantService.getBestRestaurant(itemName, quantity);

        // Assert
        assertNotNull(restaurant);
        assertEquals("Best Restaurant", restaurant.getName());
        verify(restaurantRepo, times(1)).findSuitableRestaurantByMenuItemPrice(itemName, quantity);
    }

    // Test for useCapacity
    @Test
    void testUseCapacity_shouldUpdateCapacityInUse() {
        // Arrange
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName("Test Restaurant");
        mockRestaurant.setCapacityInUse(10);
        int additionalItems = 5;

        // Act
        restaurantService.useCapacity(mockRestaurant, additionalItems);

        // Assert
        assertEquals(15, mockRestaurant.getCapacityInUse());
        verify(restaurantRepo, times(1)).save(mockRestaurant);
    }

    // Test for addCapacity
    @Test
    void testAddCapacity_shouldReduceCapacityInUse() {
        // Arrange
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setName("Test Restaurant");
        mockRestaurant.setCapacityInUse(15);
        int removedItems = 5;

        // Act
        restaurantService.addCapacity(mockRestaurant, removedItems);

        // Assert
        assertEquals(10, mockRestaurant.getCapacityInUse());
        verify(restaurantRepo, times(1)).save(mockRestaurant);
    }
}

