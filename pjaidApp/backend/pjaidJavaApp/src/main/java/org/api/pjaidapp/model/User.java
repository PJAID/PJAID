package org.api.pjaidapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import java.time.LocalDateTime;

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
    private String role;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "current_load", nullable = false)
    private int currentLoad = 0;

    @Column(name = "zmiana")
    private String zmiana;

    @Column(nullable = false)
    private boolean loggedIn = false;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> ticketQueue;

    public boolean isLoggedIn() {
        return this.loggedIn;

    }

}