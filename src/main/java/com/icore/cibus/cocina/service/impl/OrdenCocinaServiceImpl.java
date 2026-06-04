package com.icore.cibus.cocina.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.cocina.dto.request.ActualizarEstadoOrdenCocinaRequest;
import com.icore.cibus.cocina.dto.response.OrdenCocinaResponse;
import com.icore.cibus.cocina.entity.OrdenCocina;
import com.icore.cibus.cocina.repository.OrdenCocinaRepository;
import com.icore.cibus.cocina.service.OrdenCocinaService;
import com.icore.cibus.pedidos.entity.Pedido;
import com.icore.cibus.shared.enums.EstadoOrdenCocina;
import com.icore.cibus.shared.enums.EstadoPedido;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenCocinaServiceImpl implements OrdenCocinaService {

    private final OrdenCocinaRepository ordenCocinaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCocinaResponse> listar() {
        return ordenCocinaRepository.findAll()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCocinaResponse obtenerPorId(Long id) {
        return mapearResponse(obtenerOrden(id));
    }

    @Override
    @Transactional
    public OrdenCocinaResponse actualizarEstado(Long id, ActualizarEstadoOrdenCocinaRequest request) {
        OrdenCocina orden = obtenerOrden(id);
        EstadoOrdenCocina nuevoEstado = request.getEstado();

        orden.setEstado(nuevoEstado);
        sincronizarFechasOrden(orden, nuevoEstado);
        sincronizarEstadoPedido(orden.getPedido(), nuevoEstado);

        return mapearResponse(orden);
    }

    private void sincronizarFechasOrden(OrdenCocina orden, EstadoOrdenCocina estado) {
        LocalDateTime ahora = LocalDateTime.now();
        if (estado == EstadoOrdenCocina.EN_PREPARACION && orden.getFechaInicio() == null) {
            orden.setFechaInicio(ahora);
        }
        if (estado == EstadoOrdenCocina.LISTA && orden.getFechaFinalizacion() == null) {
            orden.setFechaFinalizacion(ahora);
        }
    }

    private void sincronizarEstadoPedido(Pedido pedido, EstadoOrdenCocina estadoOrden) {
        if (estadoOrden == EstadoOrdenCocina.EN_PREPARACION) {
            pedido.setEstado(EstadoPedido.EN_PREPARACION);
        }
        if (estadoOrden == EstadoOrdenCocina.LISTA) {
            pedido.setEstado(EstadoPedido.LISTO);
        }
        if (estadoOrden == EstadoOrdenCocina.CANCELADA) {
            pedido.setEstado(EstadoPedido.CANCELADO);
        }
    }

    private OrdenCocina obtenerOrden(Long id) {
        return ordenCocinaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Orden de cocina no encontrada"));
    }

    private OrdenCocinaResponse mapearResponse(OrdenCocina orden) {
        return OrdenCocinaResponse.builder()
                .id(orden.getId())
                .estado(orden.getEstado())
                .fechaCreacion(orden.getFechaCreacion())
                .fechaInicio(orden.getFechaInicio())
                .fechaFinalizacion(orden.getFechaFinalizacion())
                .observaciones(orden.getObservaciones())
                .pedidoId(orden.getPedido().getId())
                .estadoPedido(orden.getPedido().getEstado())
                .build();
    }
}
