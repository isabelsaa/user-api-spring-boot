package com.example.learning_spring_boot.service;

import com.example.learning_spring_boot.repository.UserRepository;
import com.example.learning_spring_boot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userDao;

    @Autowired
    public UserService(UserRepository userDao) {
        this.userDao = userDao;
    }
    
    public List<User> getAllUsers(Optional<String> gender) {
        List<User> users = userDao.selectAllUsers();
        if (gender.isEmpty()) {
            return users;
        }

        try {
            User.Gender theGender = User.Gender.valueOf(gender.get().toUpperCase());
            return users.stream().filter(user -> user.getGender().equals(theGender)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid gender", e);
        }

    }

    public Optional<User> getUserByUUid(UUID userUUid) {
        return userDao.selectUserByUuid(userUUid);
    }

    public int updateUser(User user) {
        Optional<User> userOptional = getUserByUUid(user.getUserUUid());
        if (userOptional.isPresent()) {
            return userDao.updateUser(user);
        }
        return -1;
    }

    public int removeUser(UUID userUUid) {
        Optional<User> userOptional = getUserByUUid(userUUid);
        if (userOptional.isPresent()) {
            return userDao.deleteUserByUuid(userUUid);
        }
        return -1;
    }

    public int insertUser(User user) {
        UUID userUUid = UUID.randomUUID();
        user.setUserUUid(userUUid);
        return userDao.insertUser(userUUid, user);
    }
}
