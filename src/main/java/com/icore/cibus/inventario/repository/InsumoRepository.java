package com.icore.cibus.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.icore.cibus.inventario.entity.Insumo;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    Optional<Insumo> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    @Query("select i from Insumo i where i.stockActual <= i.stockMinimo")
    List<Insumo> findInsumosConStockBajo();
}
