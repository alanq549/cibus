package com.icore.cibus.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.productos.entity.RecetaProducto;

public interface RecetaProductoRepository extends JpaRepository<RecetaProducto, Long> {

    List<RecetaProducto> findByProductoId(Long productoId);

    List<RecetaProducto> findByInsumoId(Long insumoId);
}
