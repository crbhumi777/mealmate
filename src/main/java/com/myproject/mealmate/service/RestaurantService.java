package com.myproject.mealmate.service;

import com.myproject.mealmate.dao.ItemRepo;
import com.myproject.mealmate.dao.RestaurantRepo;
import com.myproject.mealmate.dto.Item;
import com.myproject.mealmate.dto.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Autowired
    private ItemRepo itemRepo;

    public void addOrUpdate(String name, Map<String, Integer> menu, int totalCapacity) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setItemProcessingCapacity(totalCapacity);
        List<Item> items = new ArrayList<>();
        for (String item : menu.keySet()) {
            Item i = new Item();
            i.setName(item);
            i.setPrice(menu.get(item));
            items.add(i);
            i.setRestaurant(restaurant);
        }
        restaurant.setMenu(items);
        restaurantRepo.save(restaurant);
    }

    public List<Restaurant> getAll() {
        return (List<Restaurant>) restaurantRepo.findAll();
    }

    public Restaurant getByName(String name) {
        return restaurantRepo.findByName(name);
    }

    public Item getItemByItemNameAndRestaurantName(String itemName, Restaurant restaurant) {
        return itemRepo.findByNameAndRestaurant(itemName, restaurant);
    }

    public void updateMenu(String name, Map<String, Integer> menu) {
        for (String i : menu.keySet()) {
            Restaurant restaurant = restaurantRepo.findByName(name);
            Item item = getItemByItemNameAndRestaurantName(i, restaurant);
            if (item != null) {
                item.setPrice(menu.get(i));
                itemRepo.save(item);
            }
        }
    }

    public Restaurant getBestRestaurant(String item, int quantity) {
        return restaurantRepo.findSuitableRestaurantByMenuItemPrice(item, quantity).orElse(null);
    }

    public void useCapacity(Restaurant restaurant, int items) {
        restaurant.setCapacityInUse(restaurant.getCapacityInUse() + items);
        restaurantRepo.save(restaurant);
    }

    public void addCapacity(Restaurant restaurant, int items) {
        restaurant.setCapacityInUse(restaurant.getCapacityInUse() - items);
        restaurantRepo.save(restaurant);
    }

}
