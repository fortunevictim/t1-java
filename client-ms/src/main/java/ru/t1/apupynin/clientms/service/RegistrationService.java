package ru.t1.apupynin.clientms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.apupynin.clientms.annotation.LogDatasourceError;
import ru.t1.apupynin.clientms.dto.RegistrationRequest;
import ru.t1.apupynin.clientms.entity.Client;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.repository.ClientRepository;
import ru.t1.apupynin.clientms.repository.BlacklistRegistryRepository;

import java.time.LocalDateTime;
import ru.t1.apupynin.clientms.repository.UserRepository;

@Service
public class RegistrationService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final BlacklistRegistryRepository blacklistRepository;

    public RegistrationService(ClientRepository clientRepository, UserRepository userRepository, BlacklistRegistryRepository blacklistRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.blacklistRepository = blacklistRepository;
    }

    @LogDatasourceError
    private boolean isInBlacklist(RegistrationRequest req) {
        LocalDateTime now = LocalDateTime.now();
        return blacklistRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateAfter(
                req.documentType,
                req.documentId,
                now
        ).isPresent() || blacklistRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateIsNull(
                req.documentType,
                req.documentId
        ).isPresent();
    }

    @Transactional
    @LogDatasourceError
    public User register(RegistrationRequest req) {
        if (isInBlacklist(req)) {
            throw new IllegalArgumentException("Client is in blacklist");
        }

        User user = new User(req.login, req.password, req.email);
        user = userRepository.save(user);

        Client client = new Client(
                req.clientId,
                user.getId(),
                req.firstName,
                req.middleName,
                req.lastName,
                req.dateOfBirth,
                req.documentType,
                req.documentId,
                req.documentPrefix,
                req.documentSuffix
        );
        clientRepository.save(client);

        return user;
    }
}



