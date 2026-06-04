package com.icore.cibus.pagos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icore.cibus.pagos.dto.request.CrearPagoRequest;
import com.icore.cibus.pagos.dto.response.PagoResponse;
import com.icore.cibus.pagos.service.PagoService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<ApiResponse<PagoResponse>> crear(@Valid @RequestBody CrearPagoRequest request) {
        PagoResponse pago = pagoService.crear(request);
        return ResponseEntity.created(URI.create("/api/pagos/" + pago.getId()))
                .body(ApiResponse.<PagoResponse>builder()
                        .exito(true)
                        .mensaje("Pago registrado correctamente")
                        .datos(pago)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PagoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<PagoResponse>>builder()
                .exito(true)
                .mensaje("Pagos obtenidos correctamente")
                .datos(pagoService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PagoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<PagoResponse>builder()
                .exito(true)
                .mensaje("Pago obtenido correctamente")
                .datos(pagoService.obtenerPorId(id))
                .build());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<ApiResponse<List<PagoResponse>>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(ApiResponse.<List<PagoResponse>>builder()
                .exito(true)
                .mensaje("Pagos del pedido obtenidos correctamente")
                .datos(pagoService.listarPorPedido(pedidoId))
                .build());
    }
}
