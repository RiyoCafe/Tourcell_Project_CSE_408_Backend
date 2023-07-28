package com.example.demo_1.Repository;

import com.example.demo_1.Entity.ERole;
import com.example.demo_1.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
