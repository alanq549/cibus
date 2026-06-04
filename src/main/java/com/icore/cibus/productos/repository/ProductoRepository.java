package com.icore.cibus.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.productos.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
