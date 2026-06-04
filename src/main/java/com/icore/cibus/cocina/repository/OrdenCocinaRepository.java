package com.icore.cibus.cocina.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.cocina.entity.OrdenCocina;
import com.icore.cibus.shared.enums.EstadoOrdenCocina;

public interface OrdenCocinaRepository extends JpaRepository<OrdenCocina, Long> {

    Optional<OrdenCocina> findByPedidoId(Long pedidoId);

    List<OrdenCocina> findByEstado(EstadoOrdenCocina estado);
}
