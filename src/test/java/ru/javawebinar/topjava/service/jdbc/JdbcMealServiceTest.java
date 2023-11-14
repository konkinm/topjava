package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import java.util.Arrays;
import java.util.Objects;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {

    @Autowired
    Environment env;

    @Override
    public void createWithException() throws Exception {
        Assume.assumeFalse(Arrays.asList(env.getActiveProfiles()).contains(JDBC));
    }
}