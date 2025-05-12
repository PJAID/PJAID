package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    List<User> findByRoleAndActiveTrueAndZmianaOrderByCurrentLoadAsc(String technician, String zmiana);

    List<User> findByRoleAndActiveTrueOrderByCurrentLoadAsc(String technician);
}
