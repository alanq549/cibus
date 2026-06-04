package com.icore.cibus.productos.service;

import java.util.List;

import com.icore.cibus.productos.dto.request.ActualizarProductoRequest;
import com.icore.cibus.productos.dto.request.CrearProductoRequest;
import com.icore.cibus.productos.dto.response.ProductoResponse;

public interface ProductoService {

    ProductoResponse crear(CrearProductoRequest request);

    ProductoResponse obtenerPorId(Long id);

    List<ProductoResponse> listar();

    ProductoResponse actualizar(Long id, ActualizarProductoRequest request);

    void eliminar(Long id);
}
