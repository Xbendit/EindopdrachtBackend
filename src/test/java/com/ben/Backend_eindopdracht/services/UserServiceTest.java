package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class UserServiceTest {

    @Mock
    private UserRepository UserRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getUser() {
    }
}