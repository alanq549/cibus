package com.icore.cibus.entregas.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icore.cibus.entregas.dto.request.ActualizarEstadoEntregaRequest;
import com.icore.cibus.entregas.dto.request.CrearEntregaRequest;
import com.icore.cibus.entregas.dto.response.EntregaResponse;
import com.icore.cibus.entregas.service.EntregaService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;

    @PostMapping
    public ResponseEntity<ApiResponse<EntregaResponse>> crear(@Valid @RequestBody CrearEntregaRequest request) {
        EntregaResponse entrega = entregaService.crear(request);
        return ResponseEntity.created(URI.create("/api/entregas/" + entrega.getId()))
                .body(ApiResponse.<EntregaResponse>builder()
                        .exito(true)
                        .mensaje("Entrega creada correctamente")
                        .datos(entrega)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EntregaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<EntregaResponse>>builder()
                .exito(true)
                .mensaje("Entregas obtenidas correctamente")
                .datos(entregaService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntregaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<EntregaResponse>builder()
                .exito(true)
                .mensaje("Entrega obtenida correctamente")
                .datos(entregaService.obtenerPorId(id))
                .build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<EntregaResponse>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoEntregaRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<EntregaResponse>builder()
                .exito(true)
                .mensaje("Estado de entrega actualizado correctamente")
                .datos(entregaService.actualizarEstado(id, request))
                .build());
    }
}
