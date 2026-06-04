package com.icore.cibus.cocina.service;

import java.util.List;

import com.icore.cibus.cocina.dto.request.ActualizarEstadoOrdenCocinaRequest;
import com.icore.cibus.cocina.dto.response.OrdenCocinaResponse;

public interface OrdenCocinaService {

    List<OrdenCocinaResponse> listar();

    OrdenCocinaResponse obtenerPorId(Long id);

    OrdenCocinaResponse actualizarEstado(Long id, ActualizarEstadoOrdenCocinaRequest request);
}
