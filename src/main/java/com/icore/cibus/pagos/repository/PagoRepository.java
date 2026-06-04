package com.icore.cibus.pagos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.pagos.entity.Pago;
import com.icore.cibus.shared.enums.EstadoPago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByPedidoId(Long pedidoId);

    List<Pago> findByEmpleadoId(Long empleadoId);

    List<Pago> findByEstado(EstadoPago estado);
}
