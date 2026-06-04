package com.icore.cibus.entregas.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.empleados.entity.Empleado;
import com.icore.cibus.empleados.repository.EmpleadoRepository;
import com.icore.cibus.entregas.dto.request.ActualizarEstadoEntregaRequest;
import com.icore.cibus.entregas.dto.request.CrearEntregaRequest;
import com.icore.cibus.entregas.dto.response.EntregaResponse;
import com.icore.cibus.entregas.entity.Entrega;
import com.icore.cibus.entregas.repository.EntregaRepository;
import com.icore.cibus.entregas.service.EntregaService;
import com.icore.cibus.pedidos.entity.Pedido;
import com.icore.cibus.pedidos.repository.PedidoRepository;
import com.icore.cibus.shared.enums.EstadoEntrega;
import com.icore.cibus.shared.enums.EstadoPedido;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;
import com.icore.cibus.shared.exception.ReglaNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntregaServiceImpl implements EntregaService {

    private final EntregaRepository entregaRepository;
    private final PedidoRepository pedidoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Override
    @Transactional
    public EntregaResponse crear(CrearEntregaRequest request) {
        Pedido pedido = obtenerPedido(request.getPedidoId());
        validarPedidoParaEntrega(pedido);
        validarEntregaNoDuplicada(pedido.getId());
        Empleado repartidor = obtenerRepartidor(request.getRepartidorId());

        Entrega entrega = Entrega.builder()
                .pedido(pedido)
                .repartidor(repartidor)
                .direccionEntrega(request.getDireccionEntrega().trim())
                .estado(EstadoEntrega.PENDIENTE)
                .build();

        return mapearResponse(entregaRepository.save(entrega));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaResponse> listar() {
        return entregaRepository.findAll()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EntregaResponse obtenerPorId(Long id) {
        return mapearResponse(obtenerEntrega(id));
    }

    @Override
    @Transactional
    public EntregaResponse actualizarEstado(Long id, ActualizarEstadoEntregaRequest request) {
        Entrega entrega = obtenerEntrega(id);
        EstadoEntrega estado = request.getEstado();

        entrega.setEstado(estado);
        sincronizarPedidoYFechas(entrega, estado);

        return mapearResponse(entrega);
    }

    private void validarPedidoParaEntrega(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.PAGADO && pedido.getEstado() != EstadoPedido.LISTO) {
            throw new ReglaNegocioException("Solo se puede crear entrega para pedidos pagados o listos para entrega");
        }
    }

    private void validarEntregaNoDuplicada(Long pedidoId) {
        if (entregaRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new ReglaNegocioException("El pedido ya tiene una entrega registrada");
        }
    }

    private void sincronizarPedidoYFechas(Entrega entrega, EstadoEntrega estado) {
        LocalDateTime ahora = LocalDateTime.now();

        if (estado == EstadoEntrega.EN_CAMINO) {
            entrega.getPedido().setEstado(EstadoPedido.EN_CAMINO);
            if (entrega.getFechaSalida() == null) {
                entrega.setFechaSalida(ahora);
            }
        }

        if (estado == EstadoEntrega.ENTREGADA) {
            entrega.getPedido().setEstado(EstadoPedido.ENTREGADO);
            if (entrega.getFechaEntrega() == null) {
                entrega.setFechaEntrega(ahora);
            }
        }

        if (estado == EstadoEntrega.CANCELADA) {
            entrega.getPedido().setEstado(EstadoPedido.PAGADO);
        }
    }

    private Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Pedido no encontrado"));
    }

    private Empleado obtenerRepartidor(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Repartidor no encontrado"));
    }

    private Entrega obtenerEntrega(Long id) {
        return entregaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Entrega no encontrada"));
    }

    private EntregaResponse mapearResponse(Entrega entrega) {
        return EntregaResponse.builder()
                .id(entrega.getId())
                .pedidoId(entrega.getPedido().getId())
                .estadoPedido(entrega.getPedido().getEstado())
                .repartidorId(entrega.getRepartidor().getId())
                .direccionEntrega(entrega.getDireccionEntrega())
                .estado(entrega.getEstado())
                .fechaSalida(entrega.getFechaSalida())
                .fechaEntrega(entrega.getFechaEntrega())
                .build();
    }
}
