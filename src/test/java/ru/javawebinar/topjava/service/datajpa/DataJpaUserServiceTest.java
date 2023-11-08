package ru.javawebinar.topjava.service.datajpa;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @BeforeClass
    public static void init() {
        implementation =  Profiles.DATAJPA;
    }

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
        List<Meal> userMeals = user.getMeals();
        MealTestData.MEAL_MATCHER.assertMatch(userMeals, MealTestData.meals);
    }

    @Test
    public void getWithNoMeals() {
        User user = service.getWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(user, guest);
        assertTrue(user.getMeals().isEmpty());
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }
}