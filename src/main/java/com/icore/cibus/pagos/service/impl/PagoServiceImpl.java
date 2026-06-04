package com.icore.cibus.pagos.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.empleados.entity.Empleado;
import com.icore.cibus.empleados.repository.EmpleadoRepository;
import com.icore.cibus.pagos.dto.request.CrearPagoRequest;
import com.icore.cibus.pagos.dto.response.PagoResponse;
import com.icore.cibus.pagos.entity.Pago;
import com.icore.cibus.pagos.repository.PagoRepository;
import com.icore.cibus.pagos.service.PagoService;
import com.icore.cibus.pedidos.entity.Pedido;
import com.icore.cibus.pedidos.repository.PedidoRepository;
import com.icore.cibus.shared.enums.EstadoPago;
import com.icore.cibus.shared.enums.EstadoPedido;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;
import com.icore.cibus.shared.exception.ReglaNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Override
    @Transactional
    public PagoResponse crear(CrearPagoRequest request) {
        Pedido pedido = obtenerPedido(request.getPedidoId());
        Empleado empleado = obtenerEmpleado(request.getEmpleadoId());

        if (request.getMonto().compareTo(pedido.getTotal()) > 0) {
            throw new ReglaNegocioException("El monto del pago no puede superar el total del pedido");
        }
        if (request.getEstado() == EstadoPago.PAGADO && request.getMonto().compareTo(pedido.getTotal()) != 0) {
            throw new ReglaNegocioException("El monto confirmado debe coincidir con el total del pedido");
        }

        Pago pago = Pago.builder()
                .pedido(pedido)
                .empleado(empleado)
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .estado(request.getEstado())
                .fechaPago(LocalDateTime.now())
                .build();

        if (request.getEstado() == EstadoPago.PAGADO) {
            pedido.setEstado(EstadoPedido.PAGADO);
        }

        return mapearResponse(pagoRepository.save(pago));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listar() {
        return pagoRepository.findAll()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse obtenerPorId(Long id) {
        return mapearResponse(obtenerPago(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorPedido(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new EntidadNoEncontradaException("Pedido no encontrado");
        }
        return pagoRepository.findByPedidoId(pedidoId)
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    private Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Pedido no encontrado"));
    }

    private Empleado obtenerEmpleado(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Empleado no encontrado"));
    }

    private Pago obtenerPago(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Pago no encontrado"));
    }

    private PagoResponse mapearResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .pedidoId(pago.getPedido().getId())
                .empleadoId(pago.getEmpleado().getId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}
