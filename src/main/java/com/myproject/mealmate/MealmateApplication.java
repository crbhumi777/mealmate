package com.myproject.mealmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MealmateApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(MealmateApplication.class, args);

        FoodOrderSystem system = context.getBean(FoodOrderSystem.class);
        system.solve();
    }

}
