package com.icore.cibus.pedidos.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.clientes.entity.Cliente;
import com.icore.cibus.clientes.repository.ClienteRepository;
import com.icore.cibus.cocina.entity.OrdenCocina;
import com.icore.cibus.cocina.repository.OrdenCocinaRepository;
import com.icore.cibus.empleados.entity.Empleado;
import com.icore.cibus.empleados.repository.EmpleadoRepository;
import com.icore.cibus.inventario.entity.Insumo;
import com.icore.cibus.inventario.entity.MovimientoInventario;
import com.icore.cibus.inventario.repository.MovimientoInventarioRepository;
import com.icore.cibus.pedidos.dto.request.ActualizarEstadoPedidoRequest;
import com.icore.cibus.pedidos.dto.request.CrearDetallePedidoRequest;
import com.icore.cibus.pedidos.dto.request.CrearPedidoRequest;
import com.icore.cibus.pedidos.dto.response.DetallePedidoResponse;
import com.icore.cibus.pedidos.dto.response.PedidoResponse;
import com.icore.cibus.pedidos.entity.DetallePedido;
import com.icore.cibus.pedidos.entity.Pedido;
import com.icore.cibus.pedidos.repository.PedidoRepository;
import com.icore.cibus.pedidos.service.PedidoService;
import com.icore.cibus.productos.entity.Producto;
import com.icore.cibus.productos.entity.RecetaProducto;
import com.icore.cibus.productos.repository.ProductoRepository;
import com.icore.cibus.shared.enums.EstadoOrdenCocina;
import com.icore.cibus.shared.enums.EstadoPedido;
import com.icore.cibus.shared.enums.EstadoProducto;
import com.icore.cibus.shared.enums.TipoMovimientoInventario;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;
import com.icore.cibus.shared.exception.ReglaNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final OrdenCocinaRepository ordenCocinaRepository;

    @Override
    @Transactional
    public PedidoResponse crear(CrearPedidoRequest request) {
        Cliente cliente = obtenerCliente(request.getClienteId());
        Empleado empleado = obtenerEmpleadoSiAplica(request.getEmpleadoId());

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .empleado(empleado)
                .fechaPedido(LocalDateTime.now())
                .estado(EstadoPedido.PENDIENTE)
                .observaciones(request.getObservaciones())
                .total(BigDecimal.ZERO)
                .build();

        List<DetallePedido> detalles = request.getDetalles().stream()
                .map(detalleRequest -> crearDetalle(detalleRequest, pedido))
                .toList();

        detalles.forEach(detalle -> pedido.getDetalles().add(detalle));
        pedido.setTotal(calcularTotal(detalles));

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        descontarInventarioPorPedido(pedidoGuardado, empleado);
        crearOrdenCocina(pedidoGuardado);

        return mapearResponse(pedidoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse obtenerPorId(Long id) {
        return mapearResponse(obtenerPedido(id));
    }

    @Override
    @Transactional
    public PedidoResponse actualizarEstado(Long id, ActualizarEstadoPedidoRequest request) {
        Pedido pedido = obtenerPedido(id);
        pedido.setEstado(request.getEstado());
        return mapearResponse(pedido);
    }

    @Override
    @Transactional
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = obtenerPedido(id);
        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new ReglaNegocioException("El pedido ya se encuentra cancelado");
        }
        pedido.setEstado(EstadoPedido.CANCELADO);
        return mapearResponse(pedido);
    }

    private DetallePedido crearDetalle(CrearDetallePedidoRequest request, Pedido pedido) {
        Producto producto = obtenerProductoDisponible(request.getProductoId());
        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(request.getCantidad()));

        return DetallePedido.builder()
                .pedido(pedido)
                .producto(producto)
                .cantidad(request.getCantidad())
                .precioUnitario(precioUnitario)
                .subtotal(subtotal)
                .observaciones(request.getObservaciones())
                .build();
    }

    private BigDecimal calcularTotal(List<DetallePedido> detalles) {
        return detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void descontarInventarioPorPedido(Pedido pedido, Empleado empleado) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            for (RecetaProducto receta : detalle.getProducto().getRecetas()) {
                BigDecimal cantidadConsumida = receta.getCantidad().multiply(BigDecimal.valueOf(detalle.getCantidad()));
                Insumo insumo = receta.getInsumo();
                validarStockSuficiente(insumo, cantidadConsumida);
                insumo.setStockActual(insumo.getStockActual().subtract(cantidadConsumida));
                registrarMovimientoConsumo(pedido, empleado, insumo, cantidadConsumida);
            }
        }
    }

    private void validarStockSuficiente(Insumo insumo, BigDecimal cantidadConsumida) {
        if (insumo.getStockActual().compareTo(cantidadConsumida) < 0) {
            throw new ReglaNegocioException("Stock insuficiente para el insumo: " + insumo.getNombre());
        }
    }

    private void registrarMovimientoConsumo(Pedido pedido, Empleado empleado, Insumo insumo, BigDecimal cantidadConsumida) {
        MovimientoInventario movimiento = MovimientoInventario.builder()
                .insumo(insumo)
                .empleado(empleado)
                .tipo(TipoMovimientoInventario.SALIDA)
                .cantidad(cantidadConsumida)
                .motivo("Consumo por pedido #" + pedido.getId())
                .fechaMovimiento(LocalDateTime.now())
                .build();

        movimientoInventarioRepository.save(movimiento);
    }

    private void crearOrdenCocina(Pedido pedido) {
        OrdenCocina ordenCocina = OrdenCocina.builder()
                .pedido(pedido)
                .estado(EstadoOrdenCocina.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();

        ordenCocinaRepository.save(ordenCocina);
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Cliente no encontrado"));
    }

    private Empleado obtenerEmpleadoSiAplica(Long id) {
        if (id == null) {
            return null;
        }
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Empleado no encontrado"));
    }

    private Producto obtenerProductoDisponible(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Producto no encontrado"));

        if (producto.getEstado() != EstadoProducto.ACTIVO) {
            throw new ReglaNegocioException("El producto no esta disponible: " + producto.getNombre());
        }

        return producto;
    }

    private Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Pedido no encontrado"));
    }

    private PedidoResponse mapearResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(pedido.getCliente().getId())
                .clienteNombre(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido())
                .empleadoId(pedido.getEmpleado() != null ? pedido.getEmpleado().getId() : null)
                .fechaPedido(pedido.getFechaPedido())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .observaciones(pedido.getObservaciones())
                .detalles(pedido.getDetalles().stream().map(this::mapearDetalleResponse).toList())
                .build();
    }

    private DetallePedidoResponse mapearDetalleResponse(DetallePedido detalle) {
        return DetallePedidoResponse.builder()
                .id(detalle.getId())
                .productoId(detalle.getProducto().getId())
                .productoNombre(detalle.getProducto().getNombre())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(detalle.getSubtotal())
                .observaciones(detalle.getObservaciones())
                .build();
    }
}
