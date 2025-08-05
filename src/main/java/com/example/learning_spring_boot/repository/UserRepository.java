package com.example.learning_spring_boot.repository;

import com.example.learning_spring_boot.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> selectAllUsers();
    Optional <User> selectUserByUuid(UUID userUUid);
    int updateUser(User user);
    int deleteUserByUuid(UUID userUUid);
    int insertUser(UUID userUUid, User user);
}
