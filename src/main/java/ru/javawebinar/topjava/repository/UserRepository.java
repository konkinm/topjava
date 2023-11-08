package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository {
    // null if not found, when updated
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    // returns user without meals by default
    default User getWithMeals(int id) {
        return get(id);
    }

    // null if not found
    User getByEmail(String email);

    List<User> getAll();
}