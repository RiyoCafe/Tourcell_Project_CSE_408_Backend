package com.example.demo_1.Repository;

import com.example.demo_1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUuid(Long uuid);
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
