package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.models.SecurityRole;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("alice");
        existingUser.setEmail("alice@example.com");
        existingUser.setPassword("plain"); // wordt ge-encode bij save(...)
        // security roles
        List<SecurityRole> roles = new ArrayList<>();
        roles.add(new SecurityRole(10L,"ROLE_ADMIN", existingUser)); // jouw service gebruikt role.getRole() direct
        roles.add(new SecurityRole(11L,"ROLE_TRADER", existingUser ));
        existingUser.setSecurityRoles(roles);
    }

    @Test
    @DisplayName("save(): encode password en repository.save aanroepen")
    void save_encodesPassword_andPersists() {
        // arrange
        User toSave = new User();
        toSave.setUsername("bob");
        toSave.setEmail("bob@example.com");
        toSave.setPassword("secret");


        when(passwordEncoder.encode("secret")).thenReturn("bcrypt-secret");
        when(userRepository.save(ArgumentMatchers.<User>any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(42L);
            return u;
        });

        // act
        User saved = userService.save(toSave);

        // assert
        assertThat(saved.getId()).isEqualTo(42L);
        assertThat(saved.getPassword()).isEqualTo("bcrypt-secret");

        verify(passwordEncoder).encode("secret");
        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("bob") &&
                        u.getEmail().equals("bob@example.com") &&
                        u.getPassword().equals("bcrypt-secret")));
    }
}





