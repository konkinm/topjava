package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.AtomicIdCounter;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public List<Meal> getAll() {
        return repo.values().parallelStream().collect(Collectors.toList());
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() != null){
            repo.replace(meal.getId(),meal);
        } else {
            Integer id = AtomicIdCounter.nextId();
            meal.setId(id);
            repo.put(id,meal);
        }
    }

    @Override
    public void delete(Integer id) {
        repo.remove(id);
    }

    @Override
    public Meal get(Integer id) {
        return repo.get(id);
    }
}