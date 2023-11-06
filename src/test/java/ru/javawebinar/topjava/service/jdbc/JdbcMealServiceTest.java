package ru.javawebinar.topjava.service.jdbc;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(profiles = Profiles.JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
    @BeforeClass
    public static void init() {
        implementation = Profiles.JDBC;
    }
}