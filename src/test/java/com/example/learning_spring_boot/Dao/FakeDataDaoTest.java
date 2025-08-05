package com.example.learning_spring_boot.Dao;

import com.example.learning_spring_boot.model.User;
import com.example.learning_spring_boot.repository.FakeDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FakeDataDaoTest {

    private FakeDataRepository fakeDataDao;

    @BeforeEach
    void setUp() throws Exception {
        fakeDataDao = new FakeDataRepository();
    }

    @Test
    void shouldSelectAllUsers() {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);
        User user = users.getFirst();
        assertThat(user.getFirstName()).isEqualTo("Jon");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getUserUUid()).isNotNull();
    }

    @Test
    void shouldSelectUserByUuId() {
        UUID userUuid = UUID.randomUUID();
        User user = new User(userUuid, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        fakeDataDao.insertUser(userUuid, user);
        assertThat(fakeDataDao.selectAllUsers()).size().isEqualTo(2);
        Optional<User> userOptional = fakeDataDao.selectUserByUuid(userUuid);
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get()).isEqualTo(user);
    }

    @Test
    void shouldUpdateUser() {
        UUID jonUuid = fakeDataDao.selectAllUsers().getFirst().getUserUUid();
        User userJon = new User(jonUuid, "Jon", "Doe", User.Gender.MALE, 22, "test@test.com");
        fakeDataDao.updateUser(userJon);
        Optional<User> userOptional = fakeDataDao.selectUserByUuid(jonUuid);
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get()).isEqualTo(userJon);
    }

    @Test
    void shouldDeleteUserByUuid() {
        UUID jonUuid = fakeDataDao.selectAllUsers().getFirst().getUserUUid();
        fakeDataDao.deleteUserByUuid(jonUuid);
        assertThat(fakeDataDao.selectAllUsers()).size().isEqualTo(0);
        assertThat(fakeDataDao.selectUserByUuid(jonUuid).isPresent()).isFalse();
    }

    @Test
    void insertUser() {
        UUID userUuid = UUID.randomUUID();
        User user = new User(userUuid, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        fakeDataDao.insertUser(userUuid, user);
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(2);
    }
}