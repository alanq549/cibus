package com.icore.cibus.productos.service;

import java.util.List;

import com.icore.cibus.productos.dto.request.ActualizarProductoRequest;
import com.icore.cibus.productos.dto.request.CrearProductoRequest;
import com.icore.cibus.productos.dto.response.ProductoResponse;

public interface ProductoService {

    ProductoResponse crear(CrearProductoRequest request);

    List<ProductoResponse> listar();

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse actualizar(Long id, ActualizarProductoRequest request);

    ProductoResponse activar(Long id);

    ProductoResponse desactivar(Long id);

    List<ProductoResponse> listarDisponibles();
}
