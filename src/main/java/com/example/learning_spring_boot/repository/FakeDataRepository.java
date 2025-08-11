package com.example.learning_spring_boot.repository;

import com.example.learning_spring_boot.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeDataRepository implements UserRepository {

    private final Map<UUID, User> database;

    public FakeDataRepository() {
        database = new HashMap<>();
        UUID userUUid = UUID.randomUUID();
        database.put(userUUid, new User(userUUid, "Jon", "Doe", User.Gender.MALE, 22, "test@test.com"));
    }


    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<User> selectUserByUuid(UUID userUUid) {

        return Optional.ofNullable(database.get(userUUid));
    }

    @Override
    public int updateUser(User user) {
        database.put(user.getUserUUid(), user);
        return 1;
    }

    @Override
    public int deleteUserByUuid(UUID userUUid) {
        database.remove(userUUid);
        return 1;
    }

    @Override
    public int insertUser(UUID userUUid, User user) {
        database.put(userUUid, user);
        return 1;
    }
}
