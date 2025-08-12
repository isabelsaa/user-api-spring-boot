package com.example.learning_spring_boot.resource;

import com.example.learning_spring_boot.model.User;
import com.example.learning_spring_boot.service.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/api/v1/users")
public class UserResource {
    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{userUuid}"
    )
    public ResponseEntity<?> fetchUser(@PathVariable("userUuid") UUID userUuid) {
        Optional<User> userOptional = userService.getUserByUUid(userUuid);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("User not found"));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> insertNewUser(@RequestBody @Valid User user) {
        int result = userService.insertUser(user);
        URI location = URI.create("/api/v1/users/" + user.getUserUUid());

        return (result > 0)
                ? ResponseEntity.created(location).build()
                : ResponseEntity.badRequest().build();
    }

    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> updateNewUser(@RequestBody User user) {
        int result = userService.updateUser(user);

        return (result > 0)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.DELETE,
            path = "{userUuid}"
    )
    public ResponseEntity<Void> deleteUser(@PathVariable("userUuid") UUID userUuid) {
        int result = userService.removeUser(userUuid);

        return (result > 0)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    record ErrorMessage(String message) {
    }
}
