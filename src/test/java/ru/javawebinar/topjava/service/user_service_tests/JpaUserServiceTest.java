package ru.javawebinar.topjava.service.user_service_tests;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("jpa")
public class JpaUserServiceTest extends UserServiceTest {
    @BeforeClass
    public static void init() {
        results = new StringBuilder();
        implementation = "JPA";
    }
}