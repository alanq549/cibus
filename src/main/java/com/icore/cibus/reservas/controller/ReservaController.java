package com.icore.cibus.reservas.controller;

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

import com.icore.cibus.reservas.dto.request.ActualizarReservaRequest;
import com.icore.cibus.reservas.dto.request.CrearReservaRequest;
import com.icore.cibus.reservas.dto.response.ReservaResponse;
import com.icore.cibus.reservas.service.ReservaService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservaResponse>> crear(@Valid @RequestBody CrearReservaRequest request) {
        ReservaResponse reserva = reservaService.crear(request);
        return ResponseEntity.created(URI.create("/api/reservas/" + reserva.getId()))
                .body(ApiResponse.<ReservaResponse>builder()
                        .exito(true)
                        .mensaje("Reserva creada correctamente")
                        .datos(reserva)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<ReservaResponse>>builder()
                .exito(true)
                .mensaje("Reservas obtenidas correctamente")
                .datos(reservaService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ReservaResponse>builder()
                .exito(true)
                .mensaje("Reserva obtenida correctamente")
                .datos(reservaService.obtenerPorId(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarReservaRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<ReservaResponse>builder()
                .exito(true)
                .mensaje("Reserva actualizada correctamente")
                .datos(reservaService.actualizar(id, request))
                .build());
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<ReservaResponse>> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ReservaResponse>builder()
                .exito(true)
                .mensaje("Reserva cancelada correctamente")
                .datos(reservaService.cancelar(id))
                .build());
    }
}
