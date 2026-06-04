package com.icore.cibus.entregas.service;

import java.util.List;

import com.icore.cibus.entregas.dto.request.ActualizarEstadoEntregaRequest;
import com.icore.cibus.entregas.dto.request.CrearEntregaRequest;
import com.icore.cibus.entregas.dto.response.EntregaResponse;

public interface EntregaService {

    EntregaResponse crear(CrearEntregaRequest request);

    List<EntregaResponse> listar();

    EntregaResponse obtenerPorId(Long id);

    EntregaResponse actualizarEstado(Long id, ActualizarEstadoEntregaRequest request);
}
