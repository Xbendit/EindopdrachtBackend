package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.UserMapper;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    // Dependency Injection
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User getUser (Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User " + id + " not found"));
    }

    public UserOutputDto updateUser(long id, UserOutputDto userOutputDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User " + id + "not found"));
        user.setUsername(userOutputDto.getUsername());
        user.setEmail(userOutputDto.getEmail());
        user.setRole(userOutputDto.getRole());

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
}
