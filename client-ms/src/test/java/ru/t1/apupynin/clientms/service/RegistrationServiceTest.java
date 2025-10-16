package ru.t1.apupynin.clientms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.apupynin.clientms.dto.RegistrationRequest;
import ru.t1.apupynin.clientms.entity.Client;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.entity.UserRole;
import ru.t1.apupynin.clientms.enums.DocumentType;
import ru.t1.apupynin.clientms.enums.Role;
import ru.t1.apupynin.clientms.repository.BlacklistRegistryRepository;
import ru.t1.apupynin.clientms.repository.ClientRepository;
import ru.t1.apupynin.clientms.repository.UserRepository;
import ru.t1.apupynin.clientms.repository.UserRoleRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BlacklistRegistryRepository blacklistRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private BlacklistService blacklistService;

    @InjectMocks
    private RegistrationService registrationService;

    private RegistrationRequest request;

    @BeforeEach
    void setUp() {
        request = new RegistrationRequest();
        request.clientId = "client-123";
        request.login = "user1";
        request.password = "pass";
        request.email = "user1@example.com";
        request.firstName = "Ivan";
        request.middleName = "Ivanovich";
        request.lastName = "Ivanov";
        request.dateOfBirth = LocalDate.of(1990, 1, 1);
        request.documentType = DocumentType.PASSPORT;
        request.documentId = "123456";
        request.documentPrefix = "AB";
        request.documentSuffix = "77";
    }

    @Test
    void register_assignsCurrentClientRole_andSavesClient() {
        when(blacklistService.isInBlacklist(request.documentType, request.documentId)).thenReturn(false);

        User persisted = new User(request.login, request.password, request.email);
        persisted.setId(10L);
        when(userRepository.save(any(User.class))).thenReturn(persisted);

        User result = registrationService.register(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(request.login, result.getLogin());

        ArgumentCaptor<UserRole> userRoleCaptor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository).save(userRoleCaptor.capture());
        UserRole savedRole = userRoleCaptor.getValue();
        assertEquals(10L, savedRole.getUserId());
        assertEquals(Role.CURRENT_CLIENT, savedRole.getRole());

        verify(clientRepository).save(any(Client.class));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_throwsIfInBlacklist() {
        when(blacklistService.isInBlacklist(request.documentType, request.documentId)).thenReturn(true);
        var ex = assertThrows(IllegalArgumentException.class, () -> registrationService.register(request));
        assertTrue(ex.getMessage().toLowerCase().contains("blacklist"));
        verifyNoInteractions(userRepository, userRoleRepository, clientRepository);
    }
}
