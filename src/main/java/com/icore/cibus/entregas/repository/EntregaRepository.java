package com.icore.cibus.entregas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.entregas.entity.Entrega;
import com.icore.cibus.shared.enums.EstadoEntrega;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findByEstado(EstadoEntrega estado);

    List<Entrega> findByRepartidorId(Long repartidorId);

    Optional<Entrega> findByPedidoId(Long pedidoId);
}
