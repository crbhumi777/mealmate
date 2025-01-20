package com.myproject.mealmate.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ORDER_DETAILS")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailsId;

    private String restaurantName;

    private String itemName;

    private int quantity;

    private int cost;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Orders orders;

}
