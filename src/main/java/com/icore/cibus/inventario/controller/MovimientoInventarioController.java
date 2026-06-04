package com.icore.cibus.inventario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icore.cibus.inventario.dto.request.RegistrarMovimientoInventarioRequest;
import com.icore.cibus.inventario.dto.response.MovimientoInventarioResponse;
import com.icore.cibus.inventario.service.MovimientoInventarioService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventario/movimientos")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    @PostMapping
    public ResponseEntity<ApiResponse<MovimientoInventarioResponse>> registrar(
            @Valid @RequestBody RegistrarMovimientoInventarioRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<MovimientoInventarioResponse>builder()
                .exito(true)
                .mensaje("Movimiento de inventario registrado correctamente")
                .datos(movimientoInventarioService.registrar(request))
                .build());
    }
}
