package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.api.pjaidapp.enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByUserName(String userName);
    List<User> findByRolesContaining(String role);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.zmiana = :shift AND u.loggedIn = true")
    List<User> findAvailableTechniciansOnShift(@Param("role") Role role, @Param("shift") String shift);




}
