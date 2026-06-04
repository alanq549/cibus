package com.icore.cibus.reservas.service;

import java.util.List;

import com.icore.cibus.reservas.dto.request.ActualizarReservaRequest;
import com.icore.cibus.reservas.dto.request.CrearReservaRequest;
import com.icore.cibus.reservas.dto.response.ReservaResponse;

public interface ReservaService {

    ReservaResponse crear(CrearReservaRequest request);

    List<ReservaResponse> listar();

    ReservaResponse obtenerPorId(Long id);

    ReservaResponse actualizar(Long id, ActualizarReservaRequest request);

    ReservaResponse cancelar(Long id);
}
