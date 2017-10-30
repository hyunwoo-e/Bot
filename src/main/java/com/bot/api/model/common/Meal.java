package com.bot.api.model.common;

import lombok.Data;

import java.util.Date;

@Data
public class Meal {
    private String WEEKDAY;
    private String FOOD_NAME;
    private String PRICE;
    private String SALES_DT;
    private String SALES_TIME;
}
