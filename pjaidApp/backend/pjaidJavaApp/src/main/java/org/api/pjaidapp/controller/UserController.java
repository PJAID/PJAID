package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.mapper.UserMapper;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    @GetMapping("/technicians")
    public ResponseEntity<List<UserDto>> getTechnicians() {
        return ResponseEntity.ok(userService.getTechnicians());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        UserDto createdUser = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto dto) {
        UserDto createdAdmin = userService.createAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        UserDto updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importUsers(@RequestBody List<UserDto> users) {
        List<User> userEntities = users.stream()
                .map(userMapper::toEntity)
                .collect(Collectors.toList());

        userService.importUsers(userEntities);
        return ResponseEntity.ok().build();
    }


}
