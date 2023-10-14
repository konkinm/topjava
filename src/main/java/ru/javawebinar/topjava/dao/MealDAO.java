package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {

    public List<Meal> getAll();

    public void save(Meal meal);

    public void delete(Integer id);

    public Meal get(Integer id);
}
