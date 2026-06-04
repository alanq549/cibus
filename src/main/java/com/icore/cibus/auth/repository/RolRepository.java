package com.icore.cibus.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.auth.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
