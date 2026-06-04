package com.icore.cibus.reservas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.reservas.entity.Mesa;
import com.icore.cibus.shared.enums.EstadoMesa;

public interface MesaRepository extends JpaRepository<Mesa, Long> {

    Optional<Mesa> findByNumero(String numero);

    List<Mesa> findByEstado(EstadoMesa estado);
}
