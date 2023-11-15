package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealService;

public class MealRestController extends AbstractMealController{
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    protected MealRestController(MealService service) {
        super(log, service);
    }
}