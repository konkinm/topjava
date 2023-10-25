package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.testdata.MealTestData.*;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.testdata.UserTestData.USER_ID;

@ContextConfiguration("classpath:spring/spring-app-jdbc.xml")
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(BREAKFAST1_ID, USER_ID);
        assertMatch(meal, breakfast1);
    }

    @Test
    public void getSomeoneElsesMeal() {
        assertThrows(NotFoundException.class, () ->
                service.get(BREAKFAST1_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(BREAKFAST1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(BREAKFAST1_ID, USER_ID));
    }

    @Test
    public void deleteSomeoneElsesMeal() {
        assertThrows(NotFoundException.class, () ->
                service.delete(BREAKFAST1_ID, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> allBetweenInclusive = service.getBetweenInclusive(LOCAL_DATE, LOCAL_DATE, USER_ID);
        assertMatch(allBetweenInclusive, supper1, dinner1, breakfast1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, supper2, dinner2, breakfast2, border_meal, supper1, dinner1, breakfast1);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(BREAKFAST1_ID ,USER_ID), getUpdated());
    }

    @Test
    public void updateSomeoneElsesMeal() {
        assertThrows(NotFoundException.class, () -> {
            Meal updated = getUpdated();
            service.update(updated, ADMIN_ID);
        });
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(BREAKFAST1_DATE_TIME, "Duplicate datetime", 100), USER_ID));
    }
}