package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.models.SecurityRole;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

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
    @DisplayName("save(): encode password en repository.save call")
    void save_encodesPassword_andPersists() {
        // ARRANGE
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

        //ACT
        User saved = userService.save(toSave);
        //ASSERT
        assertThat(saved.getId()).isEqualTo(42L);
        assertThat(saved.getPassword()).isEqualTo("bcrypt-secret");

        verify(passwordEncoder).encode("secret");
        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("bob") &&
                        u.getEmail().equals("bob@example.com") &&
                        u.getPassword().equals("bcrypt-secret")));
    }

    @Test
    @DisplayName("findAll(): gives list of users")
    void findAll_returnsUsers() {
        // ARRANGE
        when(userRepository.findAll()).thenReturn(List.of(existingUser));
        //ACT
        List<User> result = userService.findAll();
        //ASSERT
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("alice");
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("getUser():find user by id")
    void getUser_found() {
        // ARRANGE
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        //ACT
        User result = userService.getUser(1L);
        //ASSERT
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        verify(userRepository).findById(1L);

    }

    @Test
    @DisplayName("getUser(): calls RecordNotFoundException when there is no id")
    void getUser_notFound_throws() {
        // ARRANGE
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        //ACT + ASSERT
        assertThatThrownBy(() -> userService.getUser(99L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("User 99 not found");
        verify(userRepository).findById(99L);
    }


    @Test
    @DisplayName("updateUser(): update username and email and maps to output DTO")
    void updateUser_succes() {
        // ARRANGE
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserOutputDto input = new UserOutputDto();
        input.setUsername("alice-new");
        input.setEmail("alice-new@example.com");
        //ACT
        UserOutputDto out = userService.updateUser(1L, input);
        //ASSERT
        assertThat(out.getId()).isEqualTo(existingUser.getId());
        assertThat(out.getUsername()).isEqualTo("alice-new");
        assertThat(out.getEmail()).isEqualTo("alice-new@example.com");

        verify(userRepository).findById(1L);
        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("alice-new") &&
                        u.getEmail().equals("alice-new@example.com")));

    }

    @Test
    @DisplayName("updateUser(): calls RecordNotFoundException when there is no user")
    void updateUser_notFound_throws() {
        // ARRANGE
        when(userRepository.findById(123L)).thenReturn(Optional.empty());

        UserOutputDto dto = new UserOutputDto();
        dto.setUsername("x");
        dto.setEmail("x@example.com");
        //ACT
        assertThatThrownBy(() -> userService.updateUser(123L, dto))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("User 123 not found");
        verify(userRepository).findById(123L);
        verify(userRepository, never()).save(any());
    }

    @Nested
    class DeleteUserTests {
        @Test
        @DisplayName("deleteUser(): delete when id exists")
        void deleteUser_success() {
            // ARRANGE
            when(userRepository.existsById(1L)).thenReturn(true);
            //ACT
            String msg = userService.deleteUser(1L);
            //ASSERT
            assertThat(msg).contains("User 1 successfully deleted");
            verify(userRepository).existsById(1L);
            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("deleteUser(): calls RecordNotFoundException when id not exists")
        void deleteUser_notFound_throws() {
            // ARRANGE
            when(userRepository.existsById(77L)).thenReturn(false);
            //ACT + ASSERT
            assertThatThrownBy(() -> userService.deleteUser(77L))
                    .isInstanceOf(RecordNotFoundException.class)
                    .hasMessageContaining("User 77 not found!");
            verify(userRepository).existsById(77L);
            verify(userRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    class LoadUserByUsernameTests {
        @Test
        @DisplayName("loadUserByUsername(): calls UserDetails with authorities from SecurityRoles")
        void loadUserByUsername_success() {
            // ARRANGE
            when(userRepository.findByUsername("alice"))
                    .thenReturn(Optional.of(existingUser));
            //ACT
            UserDetails ud = userService.loadUserByUsername("alice");
            //ASSERT
            assertThat(ud.getUsername()).isEqualTo("alice");
            assertThat(ud.getPassword()).isEqualTo("plain");

            assertThat(ud.getAuthorities())
                    .extracting("authority")
                    .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_TRADER");

            verify(userRepository).findByUsername("alice");
        }

        @Test
        @DisplayName("loadUserByUsername(): calls UsernameNotFoundException when user does not exists")
        void loadUserByUsername_notFound_throws() {
            // ARRANGE
            when(userRepository.findByUsername("missing"))
                    .thenReturn(Optional.empty());
            //ACT + ASSERT
            assertThatThrownBy(() -> userService.loadUserByUsername("missing"))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessageContaining("missing");
            verify(userRepository).findByUsername("missing");
        }
    }

}





