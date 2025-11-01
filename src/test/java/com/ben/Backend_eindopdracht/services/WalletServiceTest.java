package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.WalletOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.models.Wallet;
import com.ben.Backend_eindopdracht.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private Wallet existing;

    @BeforeEach
    void setUp() {
        existing = new Wallet();
        existing.setId(1L);
        existing.setWalletAdress("addr-1");
        existing.setCryptoCurrency("BTC");
        existing.setBalance(1_000L);
    }

    @Test
    @DisplayName("save(): saves wallet to repository")
    void save() {
        //ARRANGE
        Wallet toSave = new Wallet();
        toSave.setWalletAdress("addr-new");
        toSave.setCryptoCurrency("ETH");
        toSave.setBalance(250L);

        when(walletRepository.save(any(Wallet.class))).thenAnswer(inv -> {
            Wallet w = inv.getArgument(0);
            w.setId(42L);
            return w;
        });
        //ACT
        Wallet saved = walletService.save(toSave);
        //ASSERT
        assertThat(saved.getId()).isEqualTo(42L);
        verify(walletRepository).save(argThat(w ->
                w.getWalletAdress().equals("addr-new") &&
                        w.getCryptoCurrency().equals("ETH") &&
                        w.getBalance().equals(250L)));
    }

    @Test
    @DisplayName("findAll(): calls all wallets")
    void findAll() {
        //ARRANGE
        when(walletRepository.findAll()).thenReturn(List.of(existing));
        //ACT
        List<Wallet> result = walletService.findAll();
        //ASSERT
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWalletAdress()).isEqualTo("addr-1");
        verify(walletRepository).findAll();
    }

    @Test
    @DisplayName("getWallet(): find wallet by id")
    void getWallet() {
        //ARRANGE
        when(walletRepository.findById(1L)).thenReturn(Optional.of(existing));
        //ACT
        Wallet result = walletService.getWallet(1L);
        //ASSERT
        assertThat(result.getId()).isEqualTo(1L);
        verify(walletRepository).findById(1L);
    }

    @Test
    @DisplayName("getWallet(): calls RecordNotFoundException by unknown id")
    void getWallet_notFound() {
        //ARRANGE
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());
        //ACT + ASSERT
        assertThatThrownBy(() -> walletService.getWallet(99L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("Wallet 99 not found");

        verify(walletRepository).findById(99L);
    }

    @Test
    @DisplayName("updateWallet(): update fields and saves in map to OutputDTO")
    void updateWallet() {
        //ARRANGE
        when(walletRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(inv -> inv.getArgument(0));

        WalletOutputDto input = new WalletOutputDto();
        input.setId(1L); // mag genegeerd worden door service
        input.setWalletAdress("addr-updated");
        input.setCryptoCurrency("ETH");
        input.setBalance(2_000L);
        //ACT
        WalletOutputDto out = walletService.updateWallet(1L, input);
        //ASSERT
        assertThat(out.getId()).isEqualTo(1L);
        assertThat(out.getWalletAdress()).isEqualTo("addr-updated");
        assertThat(out.getCryptoCurrency()).isEqualTo("ETH");
        assertThat(out.getBalance()).isEqualTo(2_000L);

        verify(walletRepository).findById(1L);
        verify(walletRepository).save(argThat(w ->
                w.getId().equals(1L) &&
                        w.getWalletAdress().equals("addr-updated") &&
                        w.getCryptoCurrency().equals("ETH") &&
                        w.getBalance().equals(2_000L)));
    }

    @Test
    @DisplayName("updateWallet(): calls RecordNotFoundException by unkown id")
    void updateWallet_notFound() {
        //ARRANGE
        when(walletRepository.findById(123L)).thenReturn(Optional.empty());

        WalletOutputDto input = new WalletOutputDto();
        input.setWalletAdress("x");
        input.setCryptoCurrency("BTC");
        input.setBalance(10L);
        // ACT + ASSERT
        assertThatThrownBy(() -> walletService.updateWallet(123L, input))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("Wallet 123 not found");

        verify(walletRepository).findById(123L);
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteWallet(): happy path zonder user-relatie en zonder orders")
    void deleteWallet_success_noRelations() {
        // ARRANGE
        existing.setOrders(null);   // geen orders
        existing.setUsers(null);    // geen user-relatie
        when(walletRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(walletRepository).delete(any(Wallet.class));

        // ACT
        String msg = walletService.deleteWallet(1L);

        // ASSERT
        assertThat(msg).contains("Wallet 1 successfully deleted");
        verify(walletRepository).findById(1L);
        verify(walletRepository).delete(argThat(w -> w.getId().equals(1L)));
    }

    @Test
    @DisplayName("deleteWallet(): met user-relatie → wordt uit user.getWallets() verwijderd")
    void deleteWallet_success_withUserRelation() {
        // ARRANGE
        User u = new User();
        u.setWallets(new ArrayList<>()) ;
        u.getWallets().add(existing);  // user bevat deze wallet
        existing.setUsers(u);
        existing.setOrders(new ArrayList<>()); // geen orders

        when(walletRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(walletRepository).delete(any(Wallet.class));

        // ACT
        String msg = walletService.deleteWallet(1L);

        // ASSERT
        assertThat(msg).contains("Wallet 1 successfully deleted");
        assertThat(u.getWallets()).doesNotContain(existing); // relatie verbroken
        verify(walletRepository).delete(argThat(w -> w.getId().equals(1L)));
    }

    @Test
    @DisplayName("deleteWallet(): orders aanwezig → throw RecordNotFoundException met melding")
    void deleteWallet_ordersPresent_throws() {
        // ARRANGE
        List<com.ben.Backend_eindopdracht.models.Order> orders = new ArrayList<>();
        orders.add(mock(com.ben.Backend_eindopdracht.models.Order.class));
        existing.setOrders(orders);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(existing));

        // ACT + ASSERT
        assertThatThrownBy(() -> walletService.deleteWallet(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("heeft nog orders");

        verify(walletRepository).findById(1L);
        verify(walletRepository, never()).delete(any(Wallet.class));
    }

    @Test
    @DisplayName("deleteWallet(): calls RecordNotFoundException by unknown")
    void deleteWallet_notFound() {
        //ARRANGE
        when(walletRepository.findById(77L)).thenReturn(Optional.empty());
        //ACT + ASSERT
        assertThatThrownBy(() -> walletService.deleteWallet(77L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("Wallet 77 not found");

        verify(walletRepository).findById(77L);
        verify(walletRepository, never()).deleteById(anyLong());
    }
}