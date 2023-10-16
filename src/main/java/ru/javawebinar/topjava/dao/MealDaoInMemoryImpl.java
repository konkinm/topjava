package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.AtomicIdCounter;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealDaoInMemoryImpl implements MealDAO{

    private Map<Integer,Meal> repo = new ConcurrentHashMap<>();

    private void init() {
        repo.put(0, new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        repo.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        repo.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        repo.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        repo.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        repo.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        repo.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    {
        this.init();
    }
    @Override
    public Collection<Meal> getAll() {
        return repo.values();
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(AtomicIdCounter.nextId());
            repo.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repo.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer id) {
        return repo.remove(id) != null;
    }

    @Override
    public Meal get(Integer id) {
        return repo.get(id);
    }
}