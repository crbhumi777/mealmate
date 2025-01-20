package com.myproject.mealmate.dao;

import com.myproject.mealmate.dto.Item;
import com.myproject.mealmate.dto.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepo extends CrudRepository<Item, Long> {

    @Query("""
            SELECT i FROM
            com.myproject.mealmate.dto.Item i
            where i.name = :name AND
            i.restaurant = :restaurant
            """)
    Item findByNameAndRestaurant(String name, Restaurant restaurant);
}
