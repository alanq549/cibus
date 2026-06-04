package com.icore.cibus.pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.pedidos.entity.Pedido;
import com.icore.cibus.shared.enums.EstadoPedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByEmpleadoId(Long empleadoId);
}
