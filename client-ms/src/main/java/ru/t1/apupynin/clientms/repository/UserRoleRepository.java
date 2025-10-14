package ru.t1.apupynin.clientms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.clientms.entity.UserRole;
import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserId(Long userId);
}



