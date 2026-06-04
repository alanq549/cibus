package com.icore.cibus.inventario.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icore.cibus.inventario.dto.request.CrearInsumoRequest;
import com.icore.cibus.inventario.dto.response.InsumoResponse;
import com.icore.cibus.inventario.service.InsumoService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InsumoService insumoService;

    @PostMapping
    public ResponseEntity<ApiResponse<InsumoResponse>> crear(@Valid @RequestBody CrearInsumoRequest request) {
        InsumoResponse insumo = insumoService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/insumos/" + insumo.getId()))
                .body(ApiResponse.<InsumoResponse>builder()
                        .exito(true)
                        .mensaje("Insumo creado correctamente")
                        .datos(insumo)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InsumoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<InsumoResponse>>builder()
                .exito(true)
                .mensaje("Insumos obtenidos correctamente")
                .datos(insumoService.listar())
                .build());
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<ApiResponse<List<InsumoResponse>>> listarConStockBajo() {
        return ResponseEntity.ok(ApiResponse.<List<InsumoResponse>>builder()
                .exito(true)
                .mensaje("Insumos con stock bajo obtenidos correctamente")
                .datos(insumoService.listarConStockBajo())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InsumoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<InsumoResponse>builder()
                .exito(true)
                .mensaje("Insumo obtenido correctamente")
                .datos(insumoService.obtenerPorId(id))
                .build());
    }
}
