package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.UserMapper;
import com.ben.Backend_eindopdracht.models.SecurityRole;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

///  Added implements...
@Service
public class UserService implements UserDetailsService {

    // Dependency Injection
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User getUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User " + id + " not found"));
    }

    public UserOutputDto updateUser(long id, UserOutputDto userOutputDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User " + id + " not found"));
        user.setUsername(userOutputDto.getUsername());
        user.setEmail(userOutputDto.getEmail());
        /*user.setRole(userOutputDto.getRole());*/

        User savedUser = userRepository.save(user);

        return UserMapper.toOutputDto(savedUser);
    }

    public String deleteUser (long id) {
        if(!userRepository.existsById(id)){
            throw new RecordNotFoundException("User "+ id + " not found!");
        }
        userRepository.deleteById(id);
        return "User "+ id + " successfully deleted";
    }
    /// /// Added
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));

        ArrayList<SimpleGrantedAuthority> authorityArrayList = new ArrayList<>();
        for (SecurityRole role: user.getSecurityRoles()){
            authorityArrayList.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorityArrayList)
                .build();
    }
}
