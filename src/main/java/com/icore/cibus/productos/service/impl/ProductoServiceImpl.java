package com.icore.cibus.productos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.productos.dto.request.ActualizarProductoRequest;
import com.icore.cibus.productos.dto.request.CrearProductoRequest;
import com.icore.cibus.productos.dto.response.ProductoResponse;
import com.icore.cibus.productos.entity.EstadoProducto;
import com.icore.cibus.productos.entity.Producto;
import com.icore.cibus.productos.repository.ProductoRepository;
import com.icore.cibus.productos.service.ProductoService;
import com.icore.cibus.shared.exception.RecursoDuplicadoException;
import com.icore.cibus.shared.exception.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public ProductoResponse crear(CrearProductoRequest request) {
        String nombre = request.getNombre().trim();
        validarNombreDisponible(nombre);

        Producto producto = Producto.builder()
                .nombre(nombre)
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .disponible(request.getDisponible() == null || request.getDisponible())
                .estado(EstadoProducto.ACTIVO)
                .build();

        return mapearResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return mapearResponse(buscarProducto(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ActualizarProductoRequest request) {
        Producto producto = buscarProducto(id);
        String nombre = request.getNombre().trim();

        if (productoRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new RecursoDuplicadoException("Ya existe un producto con el nombre indicado");
        }

        producto.setNombre(nombre);
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setDisponible(request.getDisponible());
        producto.setEstado(request.getEstado());

        return mapearResponse(producto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarProducto(id);
        producto.setEstado(EstadoProducto.INACTIVO);
        producto.setDisponible(false);
    }

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
    }

    private void validarNombreDisponible(String nombre) {
        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new RecursoDuplicadoException("Ya existe un producto con el nombre indicado");
        }
    }

    private ProductoResponse mapearResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .disponible(producto.getDisponible())
                .estado(producto.getEstado())
                .creadoEn(producto.getCreadoEn())
                .actualizadoEn(producto.getActualizadoEn())
                .build();
    }
}
