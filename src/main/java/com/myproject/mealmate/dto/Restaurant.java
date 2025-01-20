package com.myproject.mealmate.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Item> menu;

    private int itemProcessingCapacity;

    private int capacityInUse;

    public String toString() {
        return String.format("{'name': '%s', 'menu': %s, 'total_capacity': %d, 'capacity_in_use': %d}",
                name, menu, itemProcessingCapacity, capacityInUse);
    }
}
