package ru.t1.apupynin.clientms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.clientms.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
}



