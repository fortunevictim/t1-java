package ru.t1.apupynin.clientms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.apupynin.clientms.annotation.LogDatasourceError;
import ru.t1.apupynin.common.aspects.annotation.Metric;
import ru.t1.apupynin.common.aspects.annotation.Cached;
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
    private final BlacklistService blacklistService;

    public RegistrationService(ClientRepository clientRepository, UserRepository userRepository, BlacklistRegistryRepository blacklistRepository, BlacklistService blacklistService) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.blacklistRepository = blacklistRepository;
        this.blacklistService = blacklistService;
    }



    @Transactional
    @LogDatasourceError
    @Metric
    public User register(RegistrationRequest req) {
        if (blacklistService.isInBlacklist(req.documentType, req.documentId)) {
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



