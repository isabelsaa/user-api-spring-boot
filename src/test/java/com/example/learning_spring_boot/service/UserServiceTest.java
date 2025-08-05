package com.example.learning_spring_boot.service;

import com.example.learning_spring_boot.repository.FakeDataRepository;
import com.example.learning_spring_boot.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    @Mock
    private FakeDataRepository fakeDataDao;
    private UserService userService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    void shouldGetAllUsers() {
        UUID userUuid = UUID.randomUUID();
        User user = new User(userUuid, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        List<User> users = List.of(user);
        given(fakeDataDao.selectAllUsers()).willReturn(users);

        assertThat(users).hasSize(1);
        assertThat(user.getFirstName()).isEqualTo("Ana");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getUserUUid()).isNotNull();
    }

    @Test
    void shouldGetAllUsersByGender() {
        UUID annaUuid = UUID.randomUUID();
        User annauser = new User(annaUuid, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        UUID mikeUuid = UUID.randomUUID();
        User mikeuser = new User(mikeUuid, "Mike", "Smith", User.Gender.MALE, 22, "test@test.com");
        List<User> users = List.of(annauser, mikeuser);

        given(fakeDataDao.selectAllUsers()).willReturn(users);

        List<User> filteredUsers = userService.getAllUsers(Optional.of(User.Gender.MALE.toString()));

        assertThat(filteredUsers).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenGenderIsInvalid() throws Exception {

        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("invalid")))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Invalid gender");
    }

    @Test
    void shouldGetUserByUUid() {
        UUID userUuid = UUID.randomUUID();
        User user = new User(userUuid, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        given(fakeDataDao.selectUserByUuid(userUuid)).willReturn(Optional.of(user));
        Optional<User> selectedUser = userService.getUserByUUid(userUuid);
        assertThat(selectedUser.isPresent()).isTrue();
        assertThat(selectedUser).hasValue(user);
        User anna = selectedUser.get();
        assertThat(anna.getFirstName()).isEqualTo("Ana");
        assertThat(anna.getLastName()).isEqualTo("Smith");
        assertThat(anna.getAge()).isEqualTo(22);
        assertThat(anna.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(anna.getEmail()).isEqualTo("test@test.com");
        assertThat(anna.getUserUUid()).isNotNull();
    }

    @Test
    void shouldUpdateUser() {
        UUID userUuid = UUID.randomUUID();
        User user = new User(userUuid, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        given(fakeDataDao.selectUserByUuid(userUuid)).willReturn(Optional.of(user));
        given(fakeDataDao.updateUser(user)).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int updateResult = fakeDataDao.updateUser(user);
        verify(fakeDataDao).updateUser(captor.capture());

        User anna = captor.getValue();

        assertUserFields(anna, updateResult);
    }

    @Test
    void shouldRemoveUser() {
        UUID userUuid = UUID.randomUUID();
        User userToDelete = new User(userUuid, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        given(fakeDataDao.selectUserByUuid(userUuid)).willReturn(Optional.of(userToDelete));
        given(fakeDataDao.deleteUserByUuid(userUuid)).willReturn(1);

        int deletedResult = userService.removeUser(userUuid);

        verify(fakeDataDao).selectUserByUuid(userUuid);
        verify(fakeDataDao).deleteUserByUuid(userUuid);

        assertThat(deletedResult).isEqualTo(1);
    }

    @Test
    void shouldInsertUser() {

        User user = new User(null, "Ana", "Smith", User.Gender.FEMALE, 22, "test@test.com");
        given(fakeDataDao.insertUser(any(UUID.class), eq(user))).willReturn(1);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        int insertResult = userService.insertUser(user);
        verify(fakeDataDao).insertUser(any(UUID.class), captor.capture());
        User user1 = captor.getValue();
        assertUserFields(user1, insertResult);
        assertThat(insertResult).isEqualTo(1);


    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    private static void assertUserFields(User anna, int updateResult) {
        assertThat(anna.getFirstName()).isEqualTo("Ana");
        assertThat(anna.getLastName()).isEqualTo("Smith");
        assertThat(anna.getAge()).isEqualTo(22);
        assertThat(anna.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(anna.getEmail()).isEqualTo("test@test.com");
        assertThat(anna.getUserUUid()).isNotNull();
        assertThat(updateResult).isEqualTo(1);
    }
}