package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.exception.UserNotFoundException;
import org.api.pjaidapp.mapper.UserMapper;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

    public UserDto createUser(UserDto dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
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

    // logowanie technika
    public void logInTechnician(Long userId) {
        User technician = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Technik nie znaleziony"));

        technician.setLoggedIn(true);
        technician.setLastLoginDate(LocalDateTime.now());
        userRepository.save(technician);
    }

    // wylogowania technika
    public void logOutTechnician(Long userId) {
        User technician = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Technik nie znaleziony"));

        technician.setLoggedIn(false);
        technician.setLastLoginDate(LocalDateTime.now());  // Możesz ustawić inną datę, jeśli chcesz
        userRepository.save(technician);
    }
}
