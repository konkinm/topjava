package ru.javawebinar.topjava.service.meal_service_tests;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "datajpa")
public class DataJpaMealServiceTest extends MealServiceTest {
    @BeforeClass
    public static void init() {
        results = new StringBuilder();
        implementation = "Data JPA";
    }
}