package org.api.pjaidapp.controller.auth;

import org.api.pjaidapp.dto.auth.AuthRequest;
import org.api.pjaidapp.dto.auth.AuthResponse;
import org.api.pjaidapp.model.RefreshToken;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.UserRepository;
import org.api.pjaidapp.security.JwtUtils;
import org.api.pjaidapp.service.RefreshTokenService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtils.generateJwtToken(user.getUserName());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String requestToken = request.get("refreshToken");

        Optional<RefreshToken> tokenOpt = refreshTokenService.findByToken(requestToken);

        if (tokenOpt.isPresent() && !refreshTokenService.isTokenExpired(tokenOpt.get())) {
            String newAccessToken = jwtUtils.generateJwtToken(tokenOpt.get().getUser().getUserName());
            return ResponseEntity.ok(new AuthResponse(newAccessToken, tokenOpt.get().getToken()));
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired refresh token");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String requestToken = request.get("refreshToken");

        refreshTokenService.findByToken(requestToken).ifPresent(rt -> {
            refreshTokenService.deleteByUser(rt.getUser());
        });

        return ResponseEntity.ok().build();
    }
}
