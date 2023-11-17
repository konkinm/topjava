package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = new ResultSetExtractor<>() {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<User> users = new ArrayList<>();
            User prevUser = null;
            while (rs.next()) {
                int id = rs.getInt("id");
                if (prevUser == null || !(prevUser.getId() == id)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRegistered(rs.getTimestamp("registered"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                    String roleAsString = rs.getString("role");
                    user.setRoles(roleAsString != null ?
                            Set.of(Role.valueOf(roleAsString)) : Collections.emptySet());
                    users.add(user);
                    prevUser = user;
                } else {
                    Set<Role> roles = prevUser.getRoles();
                    roles.add(Role.valueOf(rs.getString("role")));
                    prevUser.setRoles(roles);
                }
            }
            return users;
        }
    };
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private void batchInsert(Set<Role> roles, int user_id) {
        this.jdbcTemplate.batchUpdate(
                "insert into user_role (role, user_id) values(?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, List.copyOf(roles).get(i).name());
                        ps.setInt(2, user_id);
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    private void batchUpdate(Set<Role> roles, int user_id) {
        this.jdbcTemplate.batchUpdate(
                "update user_role set role = ? where user_id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, List.copyOf(roles).get(i).name());
                        ps.setInt(2, user_id);
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    private void deleteRolesByUserId(int user_id) {
        jdbcTemplate.update("delete from user_role where user_id = ?", user_id);
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            batchInsert(user.getRoles(), newKey.intValue());
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            deleteRolesByUserId(user.getId());
            batchUpdate(user.getRoles(), user.getId());
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE id=?",
                RESULT_SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE email=?",
                RESULT_SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_role r ON u.id = r.user_id ORDER BY name, email",
                RESULT_SET_EXTRACTOR);
    }
}