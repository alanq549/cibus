package com.icore.cibus.productos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.productos.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
