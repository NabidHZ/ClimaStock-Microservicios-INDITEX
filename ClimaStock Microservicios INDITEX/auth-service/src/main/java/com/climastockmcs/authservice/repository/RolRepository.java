package com.climastockmcs.authservice.repository;

import com.climastockmcs.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
