package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(m, 1));
        repository.get(7).setUserId(2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        } else if (repository.containsKey(meal.getId())) {
            if (Objects.equals(userId, this.get(meal.getId(), userId).getUserId())) {
                meal.setUserId(userId);
                // handle case: update, but not present in storage
                return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.containsKey(id) &&
                Objects.equals(repository.get(id).getUserId(), userId) &&
                repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if (repository.containsKey(id)) {
            Meal meal = repository.get(id);
            return Objects.equals(meal.getUserId(), userId) ? meal : null;
        } else return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values()
                .stream()
                .filter(m -> Objects.equals(m.getUserId(), userId))
                .sorted(Comparator.comparing(Meal::getDateTime)
                        .reversed())
                .collect(Collectors.toList());
    }
}