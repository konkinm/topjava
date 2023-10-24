package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int BREAKFAST1_ID = START_SEQ + 3;
    public static final int DINNER1_ID = START_SEQ + 4;
    public static final int SUPPER1_ID = START_SEQ + 5;
    public static final int BERDER_MEAL_ID = START_SEQ + 6;
    public static final int BREAKFAST2_ID = START_SEQ + 7;
    public static final int DINNER2_ID = START_SEQ + 8;
    public static final int SUPPER2_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 10;
    public static final LocalDateTime BREAKFAST1_DATE_TIME = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);
    public static final LocalDate LOCAL_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final Meal breakfast1 = new Meal(BREAKFAST1_ID, BREAKFAST1_DATE_TIME, "Завтрак", 500);
    public static final Meal dinner1 = new Meal(DINNER1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal supper1 = new Meal(SUPPER1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal border_meal = new Meal(BERDER_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal breakfast2 = new Meal(BREAKFAST2_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal dinner2 = new Meal(DINNER2_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal supper2 = new Meal(SUPPER2_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "New meal", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(breakfast1);
        updated.setDateTime(BREAKFAST1_DATE_TIME);
        updated.setDescription("Updated description");
        updated.setCalories(1000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}