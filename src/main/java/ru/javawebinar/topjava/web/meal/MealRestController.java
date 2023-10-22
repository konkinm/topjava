package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll for userId {}", authUserId());
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllBetweenDateTime(String startDate, String endDate,
                                              String startTime, String endTime) {
        log.info("getAllBetweenDateTime {} {} and {} {} for userId {}",
                startDate, endDate, startTime, endTime, authUserId());
        return MealsUtil.getTos(service.getAll(authUserId()).stream()
                .filter(m -> startDate.isEmpty() || m.getDate().isAfter(LocalDate.parse(startDate).minusDays(1)))
                .filter(m -> endDate.isEmpty() || m.getDate().isBefore(LocalDate.parse(endDate).plusDays(1)))
                .filter(m -> startTime.isEmpty() || m.getTime().isAfter(LocalTime.parse(startTime).minusMinutes(1)))
                .filter(m -> endTime.isEmpty() || m.getTime().isBefore(LocalTime.parse(endTime)))
                .collect(Collectors.toList()), authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {} for userId {}", id, authUserId());
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {} for userId {}", meal, authUserId());
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {} for userId {}", id, authUserId());
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} for userId {}", meal, id, authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}