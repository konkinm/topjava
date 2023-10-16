package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDAO {

    public Collection<Meal> getAll();

    public Meal save(Meal meal);

    public boolean delete(Integer id);

    public Meal get(Integer id);
}
