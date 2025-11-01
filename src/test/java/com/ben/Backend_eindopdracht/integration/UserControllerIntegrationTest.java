package com.ben.Backend_eindopdracht.integration;


import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        classes = com.ben.Backend_eindopdracht.BackendEindopdrachtApplication.class)
@AutoConfigureMockMvc(addFilters = true)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "jwt.SecretKey=VGhpcy1pcy1hLXZlcnktbG9uZy1zZWNyZXQta2V5LXdpdGgtMzJieXRlcw==",
        "jwt.Audience=test-audience"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String asJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    private Long createUserAndReturnId(String username, String email) throws Exception {
        var body = """
            {
              "username": "%s",
              "email": "%s",
              "password": "secret123"
            }
            """.formatted(username, email);

        var mvcResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        var response = mvcResult.getResponse().getContentAsString();
        var dto = objectMapper.readValue(response, UserOutputDto.class);
        assertThat(dto.getId()).isNotNull();
        return dto.getId();
    }

    @Test
    @DisplayName("POST /users — make user and give OutputDTO back")
    void createUser() throws Exception {
        var body = """
            {
              "username": "alice",
              "email": "alice@example.com",
              "password": "secret123"
            }
            """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @DisplayName("GET /users — calls for all users")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUsers() throws Exception {
        createUserAndReturnId("alice", "alice@example.com");

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("alice"));
    }

    @Test
    @DisplayName("GET /users/{id} — get user by id (200)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserById() throws Exception {
        Long id = createUserAndReturnId("bob", "bob@example.com");

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value("bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    @DisplayName("PUT /users/{id} — update user")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateUser_adminOnly() throws Exception {
        Long id = createUserAndReturnId("charlie", "charlie@example.com");

        var updateBody = """
            {
              "id": %d,
              "username": "charlie-upd",
              "email": "charlie-upd@example.com",
              "role": "IGNORED_IN_SERVICE"
            }
            """.formatted(id);

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value("charlie-upd"))
                .andExpect(jsonPath("$.email").value("charlie-upd@example.com"));
    }


    @Test
    @DisplayName("PUT /users/{id} — user without role")
    void updateUser_withoutAdminRole() throws Exception {
        Long id = createUserAndReturnId("dave", "dave@example.com");

        var updateBody = """
            {
              "id": %d,
              "username": "dave-upd",
              "email": "dave-upd@example.com",
              "role": "IGNORED_IN_SERVICE"
            }
            """.formatted(id);

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isForbidden());
    }
}