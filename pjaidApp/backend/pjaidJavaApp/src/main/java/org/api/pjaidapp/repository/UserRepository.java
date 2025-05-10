package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
}
