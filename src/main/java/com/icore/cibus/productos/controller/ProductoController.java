package com.icore.cibus.productos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icore.cibus.productos.dto.request.ActualizarProductoRequest;
import com.icore.cibus.productos.dto.request.CrearProductoRequest;
import com.icore.cibus.productos.dto.response.ProductoResponse;
import com.icore.cibus.productos.service.ProductoService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponse>> crear(@Valid @RequestBody CrearProductoRequest request) {
        ProductoResponse producto = productoService.crear(request);
        return ResponseEntity
                .created(URI.create("/api/v1/productos/" + producto.getId()))
                .body(ApiResponse.<ProductoResponse>builder()
                        .exito(true)
                        .mensaje("Producto creado correctamente")
                        .datos(producto)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> obtenerPorId(@PathVariable Long id) {
        ProductoResponse producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.<ProductoResponse>builder()
                .exito(true)
                .mensaje("Producto obtenido correctamente")
                .datos(producto)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> listar() {
        List<ProductoResponse> productos = productoService.listar();
        return ResponseEntity.ok(ApiResponse.<List<ProductoResponse>>builder()
                .exito(true)
                .mensaje("Productos obtenidos correctamente")
                .datos(productos)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarProductoRequest request
    ) {
        ProductoResponse producto = productoService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.<ProductoResponse>builder()
                .exito(true)
                .mensaje("Producto actualizado correctamente")
                .datos(producto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .exito(true)
                .mensaje("Producto desactivado correctamente")
                .build());
    }
}
