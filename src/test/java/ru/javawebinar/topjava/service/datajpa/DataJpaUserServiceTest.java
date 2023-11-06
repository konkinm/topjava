package ru.javawebinar.topjava.service.datajpa;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles( Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @BeforeClass
    public static void init() {
        implementation =  Profiles.DATAJPA;
    }
}