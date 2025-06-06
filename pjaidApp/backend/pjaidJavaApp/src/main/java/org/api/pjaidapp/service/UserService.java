package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.enums.Role;
import org.api.pjaidapp.exception.UserNotFoundException;
import org.api.pjaidapp.mapper.UserMapper;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final ShiftCalendarService shiftCalendarService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, ShiftCalendarService shiftCalendarService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.shiftCalendarService = shiftCalendarService;
    }

    public List<UserDto> getTechnicians() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(Role.TECHNICIAN))
                .map(userMapper::toDto)
                .toList();
    }


    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public void importUsers(List<User> users) {
        LocalDate today = LocalDate.now();
        int count = 0;

        for (User user : users) {
            String todayShift = shiftCalendarService.getShiftForDate(today);
            user.setZmiana(todayShift);
            userRepository.save(user);
            count++;
        }
        System.out.println("Zaimportowano " + count + " użytkowników ze zmianą.");
    }

    public UserDto createUser(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        if (userRepository.findByUserName(dto.getUserName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = userMapper.toEntity(dto);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }

        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());

        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public UserDto createAdmin(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        if (userRepository.findByUserName(dto.getUserName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setRoles(Set.of(Role.ADMIN)); // przypisujemy ROLE.ADMIN
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(Role.TECHNICIAN));
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
}
