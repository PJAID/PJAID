package org.api.pjaidapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.pjaidapp.enums.Role;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "username")
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(name = "active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean active = true;

    @Column(name = "logged_in", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean loggedIn = false;

    @Column(name = "zmiana", length = 10)
    private String zmiana;

    @Column(name = "current_load")
    private int currentLoad = 0;

}

