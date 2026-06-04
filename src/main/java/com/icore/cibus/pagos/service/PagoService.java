package com.icore.cibus.pagos.service;

import java.util.List;

import com.icore.cibus.pagos.dto.request.CrearPagoRequest;
import com.icore.cibus.pagos.dto.response.PagoResponse;

public interface PagoService {

    PagoResponse crear(CrearPagoRequest request);

    List<PagoResponse> listar();

    PagoResponse obtenerPorId(Long id);

    List<PagoResponse> listarPorPedido(Long pedidoId);
}
