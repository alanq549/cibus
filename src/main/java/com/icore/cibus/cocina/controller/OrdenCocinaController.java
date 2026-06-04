package com.icore.cibus.cocina.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icore.cibus.cocina.dto.request.ActualizarEstadoOrdenCocinaRequest;
import com.icore.cibus.cocina.dto.response.OrdenCocinaResponse;
import com.icore.cibus.cocina.service.OrdenCocinaService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cocina/ordenes")
@RequiredArgsConstructor
public class OrdenCocinaController {

    private final OrdenCocinaService ordenCocinaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrdenCocinaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<OrdenCocinaResponse>>builder()
                .exito(true)
                .mensaje("Ordenes de cocina obtenidas correctamente")
                .datos(ordenCocinaService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrdenCocinaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<OrdenCocinaResponse>builder()
                .exito(true)
                .mensaje("Orden de cocina obtenida correctamente")
                .datos(ordenCocinaService.obtenerPorId(id))
                .build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<OrdenCocinaResponse>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoOrdenCocinaRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<OrdenCocinaResponse>builder()
                .exito(true)
                .mensaje("Estado de orden de cocina actualizado correctamente")
                .datos(ordenCocinaService.actualizarEstado(id, request))
                .build());
    }
}
