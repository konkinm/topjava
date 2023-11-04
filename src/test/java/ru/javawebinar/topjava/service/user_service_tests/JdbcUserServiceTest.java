package ru.javawebinar.topjava.service.user_service_tests;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("jdbc")
public class JdbcUserServiceTest extends UserServiceTest {
    @BeforeClass
    public static void init() {
        results = new StringBuilder();
        implementation = "JDBC";
    }
}