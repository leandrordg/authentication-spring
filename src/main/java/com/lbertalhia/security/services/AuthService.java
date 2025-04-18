package com.lbertalhia.security.services;

import com.lbertalhia.security.dtos.LoginRequestDto;
import com.lbertalhia.security.dtos.LoginResponseDto;
import com.lbertalhia.security.dtos.RegisterUserDto;
import com.lbertalhia.security.entities.Role;
import com.lbertalhia.security.entities.User;
import com.lbertalhia.security.repositories.RoleRepository;
import com.lbertalhia.security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public ResponseEntity<User> register(RegisterUserDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already exists");
        }

        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!user.isLoginCorrect(dto, passwordEncoder)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return ResponseEntity.status(HttpStatus.OK).body(generateJwtForUser(user));
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    private LoginResponseDto generateJwtForUser(User user) {
        Instant now = Instant.now();

        String scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(300L))
                .claim("scope", scopes)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDto(token, 300L);
    }
}
