package ru.javawebinar.topjava.service.datajpa;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @BeforeClass
    public static void init() {
        implementation =  Profiles.DATAJPA;
    }

    @Test
    public void getWithMeals() {
        User user = service.get(USER_ID);
        List<Meal> userMeals = user.getMeals();
        MealTestData.MEAL_MATCHER.assertMatch(userMeals, MealTestData.meals);
    }
}