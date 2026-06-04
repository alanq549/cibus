package com.icore.cibus.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.inventario.entity.MovimientoInventario;
import com.icore.cibus.shared.enums.TipoMovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByInsumoId(Long insumoId);

    List<MovimientoInventario> findByEmpleadoId(Long empleadoId);

    List<MovimientoInventario> findByTipo(TipoMovimientoInventario tipo);
}
