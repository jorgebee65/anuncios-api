package com.dom.adv.api.config;

import com.dom.adv.api.entity.Category;
import com.dom.adv.api.entity.Role;
import com.dom.adv.api.entity.User;
import com.dom.adv.api.repository.CategoryRepository;
import com.dom.adv.api.repository.RoleRepository;
import com.dom.adv.api.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer {

    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${adv.admin.password}")
    private String rawPassword;

    public DataInitializer(
            CategoryRepository categoryRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.categoryRepository = categoryRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        initCategories();
        initRoles();
        initAdminUser();
    }

    private void initCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("INMUEBLES", "sports", "blue"),
                    new Category("MASCOTAS", "electronics", "yellow"),
                    new Category("ELECTRONICOS", "home", "red"),
                    new Category("MUEBLES", "forniture", "white"),
                    new Category("VARIOS", "several", "orange")
            );
            categoryRepository.saveAll(categories);
            System.out.println("✅ Categorías cargadas por defecto.");
        } else {
            System.out.println("📦 La tabla de categorías ya tiene datos.");
        }
    }

    private void initRoles() {
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.printf("✅ Rol '%s' creado.%n", roleName);
        } else {
            System.out.printf("📦 El rol '%s' ya existe.%n", roleName);
        }
    }

    private void initAdminUser() {
        String adminUsername = "jorgebee65";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            Optional<Role> adminRole = roleRepository.findByName("ADMIN");

            if (adminRole.isEmpty()) {
                System.err.println("❌ Rol ADMIN no encontrado. No se puede crear usuario admin.");
                return;
            }

            User user = new User();
            user.setUsername(adminUsername);
            user.setPassword(passwordEncoder.encode(rawPassword)); // ← puedes cambiar este password
            user.setFirstName("Jorge");
            user.setEnabled(true);
            user.setRoles(Set.of(adminRole.get()));

            userRepository.save(user);
            System.out.println("✅ Usuario admin 'jorgebee65' creado.");
        } else {
            System.out.println("📦 Usuario 'jorgebee65' ya existe.");
        }
    }
}