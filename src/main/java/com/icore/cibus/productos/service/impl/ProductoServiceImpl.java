package com.icore.cibus.productos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.productos.dto.request.ActualizarProductoRequest;
import com.icore.cibus.productos.dto.request.CrearProductoRequest;
import com.icore.cibus.productos.dto.response.ProductoResponse;
import com.icore.cibus.productos.entity.Categoria;
import com.icore.cibus.productos.entity.Producto;
import com.icore.cibus.productos.repository.CategoriaRepository;
import com.icore.cibus.productos.repository.ProductoRepository;
import com.icore.cibus.productos.service.ProductoService;
import com.icore.cibus.shared.enums.EstadoProducto;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;
import com.icore.cibus.shared.exception.ReglaNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public ProductoResponse crear(CrearProductoRequest request) {
        String nombre = request.getNombre().trim();
        validarNombreDisponible(nombre);
        Categoria categoria = obtenerCategoria(request.getCategoriaId());

        Producto producto = Producto.builder()
                .nombre(nombre)
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .estado(EstadoProducto.ACTIVO)
                .categoria(categoria)
                .build();

        return mapearResponse(productoRepository.save(producto));
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
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return mapearResponse(obtenerProducto(id));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ActualizarProductoRequest request) {
        Producto producto = obtenerProducto(id);
        String nombre = request.getNombre().trim();

        if (productoRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new ReglaNegocioException("Ya existe un producto con el nombre indicado");
        }

        producto.setNombre(nombre);
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(obtenerCategoria(request.getCategoriaId()));

        return mapearResponse(producto);
    }

    @Override
    @Transactional
    public ProductoResponse activar(Long id) {
        Producto producto = obtenerProducto(id);
        producto.setEstado(EstadoProducto.ACTIVO);
        return mapearResponse(producto);
    }

    @Override
    @Transactional
    public ProductoResponse desactivar(Long id) {
        Producto producto = obtenerProducto(id);
        producto.setEstado(EstadoProducto.INACTIVO);
        return mapearResponse(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarDisponibles() {
        return productoRepository.findByEstado(EstadoProducto.ACTIVO)
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Producto no encontrado"));
    }

    private Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Categoria no encontrada"));
    }

    private void validarNombreDisponible(String nombre) {
        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe un producto con el nombre indicado");
        }
    }

    private ProductoResponse mapearResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .estado(producto.getEstado())
                .categoriaId(producto.getCategoria().getId())
                .categoriaNombre(producto.getCategoria().getNombre())
                .build();
    }
}
