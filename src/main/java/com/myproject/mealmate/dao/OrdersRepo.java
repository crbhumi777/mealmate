package com.myproject.mealmate.dao;

import com.myproject.mealmate.dto.Orders;
import org.springframework.data.repository.CrudRepository;

public interface OrdersRepo extends CrudRepository<Orders, Long> {
}
