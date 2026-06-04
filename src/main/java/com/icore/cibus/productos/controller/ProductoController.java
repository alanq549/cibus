package com.icore.cibus.productos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
        return ResponseEntity.created(URI.create("/api/v1/productos/" + producto.getId()))
                .body(ApiResponse.<ProductoResponse>builder()
                        .exito(true)
                        .mensaje("Producto creado correctamente")
                        .datos(producto)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<ProductoResponse>>builder()
                .exito(true)
                .mensaje("Productos obtenidos correctamente")
                .datos(productoService.listar())
                .build());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> listarDisponibles() {
        return ResponseEntity.ok(ApiResponse.<List<ProductoResponse>>builder()
                .exito(true)
                .mensaje("Productos disponibles obtenidos correctamente")
                .datos(productoService.listarDisponibles())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ProductoResponse>builder()
                .exito(true)
                .mensaje("Producto obtenido correctamente")
                .datos(productoService.obtenerPorId(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarProductoRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<ProductoResponse>builder()
                .exito(true)
                .mensaje("Producto actualizado correctamente")
                .datos(productoService.actualizar(id, request))
                .build());
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<ProductoResponse>> activar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ProductoResponse>builder()
                .exito(true)
                .mensaje("Producto activado correctamente")
                .datos(productoService.activar(id))
                .build());
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<ProductoResponse>> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ProductoResponse>builder()
                .exito(true)
                .mensaje("Producto desactivado correctamente")
                .datos(productoService.desactivar(id))
                .build());
    }
}
