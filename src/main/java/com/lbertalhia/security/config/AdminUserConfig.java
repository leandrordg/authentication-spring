package com.lbertalhia.security.config;

import com.lbertalhia.security.entities.Role;
import com.lbertalhia.security.entities.User;
import com.lbertalhia.security.repositories.RoleRepository;
import com.lbertalhia.security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        Optional<User> userAdmin = userRepository.findByUsername("ADMIN");

        userAdmin.ifPresentOrElse(
                (user) -> {
                    System.out.println("Admin already exists");
                },
                () -> {
                    User user = new User();
                    user.setUsername("ADMIN");
                    user.setPassword(passwordEncoder.encode("123")); // OBS: senha de teste para a funcionalidade da autenticação!
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
