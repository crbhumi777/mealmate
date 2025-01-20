package com.myproject.mealmate.dao;

import com.myproject.mealmate.dto.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RestaurantRepo extends CrudRepository<Restaurant, Long> {

    Restaurant findByName(String name);

    @Query(value = """
                SELECT r.*
                FROM restaurant r
                JOIN item m ON r.id = m.id
                WHERE m.name = :name 
                AND r.item_processing_capacity - r.capacity_in_use >= :quantity
                ORDER BY m.price ASC
                LIMIT 1
            """, nativeQuery = true)
    Optional<Restaurant> findSuitableRestaurantByMenuItemPrice(@Param("name") String name,
                                                               @Param("quantity") int quantity);
}
