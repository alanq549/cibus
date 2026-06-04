package com.icore.cibus.inventario.service;

import com.icore.cibus.inventario.dto.request.RegistrarMovimientoInventarioRequest;
import com.icore.cibus.inventario.dto.response.MovimientoInventarioResponse;

public interface MovimientoInventarioService {

    MovimientoInventarioResponse registrar(RegistrarMovimientoInventarioRequest request);
}
