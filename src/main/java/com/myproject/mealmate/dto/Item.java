package com.myproject.mealmate.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String name;

    private int price;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Restaurant restaurant;

    @Override
    public String toString() {
        return "{" + name + ":" + price + "}";
    }
}
