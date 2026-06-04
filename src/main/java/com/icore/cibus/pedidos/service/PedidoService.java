package com.icore.cibus.pedidos.service;

import java.util.List;

import com.icore.cibus.pedidos.dto.request.ActualizarEstadoPedidoRequest;
import com.icore.cibus.pedidos.dto.request.CrearPedidoRequest;
import com.icore.cibus.pedidos.dto.response.PedidoResponse;

public interface PedidoService {

    PedidoResponse crear(CrearPedidoRequest request);

    List<PedidoResponse> listar();

    PedidoResponse obtenerPorId(Long id);

    PedidoResponse actualizarEstado(Long id, ActualizarEstadoPedidoRequest request);

    PedidoResponse cancelar(Long id);
}
