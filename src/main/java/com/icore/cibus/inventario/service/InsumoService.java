package com.icore.cibus.inventario.service;

import java.util.List;

import com.icore.cibus.inventario.dto.request.CrearInsumoRequest;
import com.icore.cibus.inventario.dto.response.InsumoResponse;

public interface InsumoService {

    InsumoResponse crear(CrearInsumoRequest request);

    List<InsumoResponse> listar();

    InsumoResponse obtenerPorId(Long id);

    List<InsumoResponse> listarConStockBajo();
}
