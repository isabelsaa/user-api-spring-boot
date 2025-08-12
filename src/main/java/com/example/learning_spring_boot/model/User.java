package com.example.learning_spring_boot.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class User {
    private UUID userUUid;
    @NotNull
    private final String firstName;
    @NotNull
    private final String lastName;
    @NotNull
    private final Gender gender;
    @NotNull
    @Min(value = 0)
    @Max(value = 112)
    private final Integer age;
    @NotNull
    @Email
    private final String email;

    public enum Gender {
        MALE, FEMALE
    }

    public User(UUID userUUid, String firstName, String lastName, Gender gender, Integer age, String email) {
        this.userUUid = userUUid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    public UUID getUserUUid() {
        return userUUid;
    }

    public void setUserUUid(UUID userUUid) {
        this.userUUid = userUUid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userUUid=" + userUUid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
