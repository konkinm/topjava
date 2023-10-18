package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;


import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final Integer userId = authUserId();

    @Autowired
    private MealService service;

    public List<Meal> getAll() {
        log.info("getAll for userId {}", userId);
        return service.getAll(userId);
    }

    public Meal get(int id) {
        log.info("get {} for userId {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        log.info("create {} for userId {}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        log.info("delete {} for userId {}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} for userId {}", meal, id, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }
}