package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> dayOfCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            if (!dayOfCalories.containsKey(mealDate)){
                dayOfCalories.put(mealDate,meal.getCalories());
            } else {
                dayOfCalories.replace(mealDate
                        , meal.getCalories() + dayOfCalories.get(mealDate));
            }
        }

        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            String description = meal.getDescription();
            int mealCalories = meal.getCalories();
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(),startTime,endTime)){
                if (dayOfCalories.get(mealDate) > caloriesPerDay) {
                    result.add(new UserMealWithExcess(meal.getDateTime(), description, mealCalories, true));
                } else {
                    result.add(new UserMealWithExcess(meal.getDateTime(), description, mealCalories, false));
                }
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return meals.stream()
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(),startTime,endTime))
                .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), (
                        meals.stream()
                                .collect(Collectors.collectingAndThen(
                                        Collectors.groupingBy(m1 -> m1.getDateTime().toLocalDate()),
                                        groups -> groups.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey
                                                , e -> e.getValue().stream().mapToInt(UserMeal::getCalories).sum()))))
                        .get(m.getDateTime().toLocalDate()) > caloriesPerDay)))
                .collect(Collectors.toList());
    }
}