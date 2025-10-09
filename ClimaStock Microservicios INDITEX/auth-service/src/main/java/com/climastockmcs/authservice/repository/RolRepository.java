package com.climastockmcs.authservice.repository;

import com.climastockmcs.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//JpaRepository<Role, Long> es una interfaz que proporciona métodos CRUD para la entidad Role
//y utiliza Long como tipo de dato para la clave primaria de la entidad.
public interface RolRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
