package com.icore.cibus.pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.pedidos.entity.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);

    List<DetallePedido> findByProductoId(Long productoId);
}
