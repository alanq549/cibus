package com.icore.cibus.productos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.productos.entity.Producto;
import com.icore.cibus.shared.enums.EstadoProducto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByNombreIgnoreCase(String nombre);

    List<Producto> findByEstado(EstadoProducto estado);

    List<Producto> findByCategoriaIdAndEstado(Long categoriaId, EstadoProducto estado);

    boolean existsByNombreIgnoreCase(String nombre);
}
