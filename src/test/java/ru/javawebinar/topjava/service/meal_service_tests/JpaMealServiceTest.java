package ru.javawebinar.topjava.service.meal_service_tests;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "jpa")
public class JpaMealServiceTest extends MealServiceTest {
    @BeforeClass
    public static void init() {
        results = new StringBuilder();
        implementation = "JPA";
    }
}