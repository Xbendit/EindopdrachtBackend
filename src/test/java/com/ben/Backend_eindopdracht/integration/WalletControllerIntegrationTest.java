package com.ben.Backend_eindopdracht.integration;

import com.ben.Backend_eindopdracht.dtos.UserInputDto;
import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.dtos.WalletInputDto;
import com.ben.Backend_eindopdracht.dtos.WalletOutputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WalletControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private long createUserAndReturnId() throws Exception {
        String unique = String.valueOf(System.currentTimeMillis());

        UserInputDto user = new UserInputDto();
        user.setUsername("user_" + unique);
        user.setEmail("u" + unique + "@example.com");
        user.setPassword("secret");

        MvcResult res = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        UserOutputDto created = objectMapper.readValue(
                res.getResponse().getContentAsByteArray(),
                UserOutputDto.class
        );
        return created.getId();
    }

    @Test
    @DisplayName("Wallet CRUD flow: create(assign) → list → get → update → delete")
    void wallet_crud_flow() throws Exception {
        // 1) Arrange: make user
        long userId = createUserAndReturnId();

        // 2) Create + assign to user
        WalletInputDto in = new WalletInputDto();
        in.setWalletAdress("addr-123");
        in.setCryptoCurrency("BTC");
        in.setBalance(1000L);

        MvcResult createRes = mockMvc.perform(post("/wallets/{userId}/assign", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.walletAdress").value("addr-123"))
                .andExpect(jsonPath("$.cryptoCurrency").value("BTC"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andReturn();

        WalletOutputDto created = objectMapper.readValue(
                createRes.getResponse().getContentAsByteArray(),
                WalletOutputDto.class
        );
        long walletId = created.getId();
        assertThat(walletId).isPositive();

        // 3) List
        mockMvc.perform(get("/wallets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(walletId));

        // 4) Get by id
        mockMvc.perform(get("/wallets/{id}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId))
                .andExpect(jsonPath("$.walletAdress").value("addr-123"))
                .andExpect(jsonPath("$.cryptoCurrency").value("BTC"))
                .andExpect(jsonPath("$.balance").value(1000));

        // 5) Update
        WalletOutputDto update = new WalletOutputDto();
        update.setWalletAdress("addr-999");
        update.setCryptoCurrency("ETH");
        update.setBalance(2222L);

        mockMvc.perform(put("/wallets/{id}", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId))
                .andExpect(jsonPath("$.walletAdress").value("addr-999"))
                .andExpect(jsonPath("$.cryptoCurrency").value("ETH"))
                .andExpect(jsonPath("$.balance").value(2222));

        // 6) Delete
        mockMvc.perform(delete("/wallets/{id}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("successfully deleted")));
    }
}
