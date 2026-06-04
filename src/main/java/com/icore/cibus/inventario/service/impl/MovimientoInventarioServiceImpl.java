package com.icore.cibus.inventario.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.empleados.entity.Empleado;
import com.icore.cibus.empleados.repository.EmpleadoRepository;
import com.icore.cibus.inventario.dto.request.RegistrarMovimientoInventarioRequest;
import com.icore.cibus.inventario.dto.response.MovimientoInventarioResponse;
import com.icore.cibus.inventario.entity.Insumo;
import com.icore.cibus.inventario.entity.MovimientoInventario;
import com.icore.cibus.inventario.repository.InsumoRepository;
import com.icore.cibus.inventario.repository.MovimientoInventarioRepository;
import com.icore.cibus.inventario.service.MovimientoInventarioService;
import com.icore.cibus.shared.enums.TipoMovimientoInventario;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;
import com.icore.cibus.shared.exception.ReglaNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final InsumoRepository insumoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    @Override
    @Transactional
    public MovimientoInventarioResponse registrar(RegistrarMovimientoInventarioRequest request) {
        Insumo insumo = obtenerInsumo(request.getInsumoId());
        Empleado empleado = obtenerEmpleado(request.getEmpleadoId());

        actualizarStock(insumo, request.getTipo(), request.getCantidad());

        MovimientoInventario movimiento = MovimientoInventario.builder()
                .insumo(insumo)
                .empleado(empleado)
                .tipo(request.getTipo())
                .cantidad(request.getCantidad())
                .motivo(request.getMotivo())
                .fechaMovimiento(LocalDateTime.now())
                .build();

        return mapearResponse(movimientoInventarioRepository.save(movimiento));
    }

    private void actualizarStock(Insumo insumo, TipoMovimientoInventario tipo, BigDecimal cantidad) {
        BigDecimal stockActual = insumo.getStockActual();

        switch (tipo) {
            case ENTRADA -> {
                validarCantidadPositiva(cantidad);
                insumo.setStockActual(stockActual.add(cantidad));
            }
            case SALIDA, MERMA -> {
                validarCantidadPositiva(cantidad);
                descontarStock(insumo, cantidad);
            }
            case AJUSTE -> insumo.setStockActual(cantidad);
        }
    }

    private void validarCantidadPositiva(BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La cantidad debe ser mayor a cero para este movimiento");
        }
    }

    private void descontarStock(Insumo insumo, BigDecimal cantidad) {
        if (insumo.getStockActual().compareTo(cantidad) < 0) {
            throw new ReglaNegocioException("No hay stock suficiente para registrar el movimiento");
        }
        insumo.setStockActual(insumo.getStockActual().subtract(cantidad));
    }

    private Insumo obtenerInsumo(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Insumo no encontrado"));
    }

    private Empleado obtenerEmpleado(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Empleado no encontrado"));
    }

    private MovimientoInventarioResponse mapearResponse(MovimientoInventario movimiento) {
        return MovimientoInventarioResponse.builder()
                .id(movimiento.getId())
                .tipo(movimiento.getTipo())
                .cantidad(movimiento.getCantidad())
                .motivo(movimiento.getMotivo())
                .fechaMovimiento(movimiento.getFechaMovimiento())
                .insumoId(movimiento.getInsumo().getId())
                .insumoNombre(movimiento.getInsumo().getNombre())
                .empleadoId(movimiento.getEmpleado().getId())
                .stockActual(movimiento.getInsumo().getStockActual())
                .build();
    }
}
