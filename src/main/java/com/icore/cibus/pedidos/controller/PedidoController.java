package com.icore.cibus.pedidos.controller;

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

import com.icore.cibus.pedidos.dto.request.ActualizarEstadoPedidoRequest;
import com.icore.cibus.pedidos.dto.request.CrearPedidoRequest;
import com.icore.cibus.pedidos.dto.response.PedidoResponse;
import com.icore.cibus.pedidos.service.PedidoService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<ApiResponse<PedidoResponse>> crear(@Valid @RequestBody CrearPedidoRequest request) {
        PedidoResponse pedido = pedidoService.crear(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + pedido.getId()))
                .body(ApiResponse.<PedidoResponse>builder()
                        .exito(true)
                        .mensaje("Pedido creado correctamente")
                        .datos(pedido)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<PedidoResponse>>builder()
                .exito(true)
                .mensaje("Pedidos obtenidos correctamente")
                .datos(pedidoService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<PedidoResponse>builder()
                .exito(true)
                .mensaje("Pedido obtenido correctamente")
                .datos(pedidoService.obtenerPorId(id))
                .build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<PedidoResponse>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoPedidoRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<PedidoResponse>builder()
                .exito(true)
                .mensaje("Estado del pedido actualizado correctamente")
                .datos(pedidoService.actualizarEstado(id, request))
                .build());
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<PedidoResponse>> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<PedidoResponse>builder()
                .exito(true)
                .mensaje("Pedido cancelado correctamente")
                .datos(pedidoService.cancelar(id))
                .build());
    }
}
