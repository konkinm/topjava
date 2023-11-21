package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = new ResultSetExtractor<>() {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new LinkedHashMap<>();
            while (rs.next()) {
                User parsedUser = ROW_MAPPER.mapRow(rs, rs.getRow());
                int id = parsedUser.getId();
                String roleAsString = rs.getString("role");
                Role parsedRole = roleAsString == null ? null : Role.valueOf(roleAsString);

                users.computeIfAbsent(id, uId -> {
                    parsedUser.setRoles(parsedRole != null ? Collections.singleton(parsedRole) : Collections.emptySet());
                    return parsedUser;
                });

                users.computeIfPresent(id, (uId, u) -> {
                    if (parsedRole != null) {
                        u.getRoles().add(parsedRole);
                    }
                    return u;
                });

            }
            return new ArrayList<>(users.values());
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

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            batchRoleInsert(EnumSet.copyOf(user.getRoles()), newKey.intValue());
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            deleteRolesByUserId(user.getId());
            batchRoleInsert(user.getRoles(), user.getId());
        }
        return user;
    }

    private void batchRoleInsert(Set<Role> roles, int user_id) {
        if (!roles.isEmpty()) {
                this.jdbcTemplate.batchUpdate(
                        "INSERT INTO user_role (role, user_id) VALUES(?,?)",
                new BatchPreparedStatementSetter() {
                    final List<Role> roleList =  List.copyOf(roles);
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, roleList.get(i).name());
                        ps.setInt(2, user_id);
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
        }
    }

    private void deleteRolesByUserId(int user_id) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", user_id);
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