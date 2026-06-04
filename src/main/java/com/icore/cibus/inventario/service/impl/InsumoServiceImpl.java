package com.icore.cibus.inventario.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.inventario.dto.request.CrearInsumoRequest;
import com.icore.cibus.inventario.dto.response.InsumoResponse;
import com.icore.cibus.inventario.entity.Insumo;
import com.icore.cibus.inventario.repository.InsumoRepository;
import com.icore.cibus.inventario.service.InsumoService;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;
import com.icore.cibus.shared.exception.ReglaNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsumoServiceImpl implements InsumoService {

    private final InsumoRepository insumoRepository;

    @Override
    @Transactional
    public InsumoResponse crear(CrearInsumoRequest request) {
        String nombre = request.getNombre().trim();
        if (insumoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe un insumo con el nombre indicado");
        }

        Insumo insumo = Insumo.builder()
                .nombre(nombre)
                .stockActual(request.getStockActual())
                .stockMinimo(request.getStockMinimo())
                .unidadMedida(request.getUnidadMedida().trim())
                .build();

        return mapearResponse(insumoRepository.save(insumo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoResponse> listar() {
        return insumoRepository.findAll()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InsumoResponse obtenerPorId(Long id) {
        return mapearResponse(obtenerInsumo(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoResponse> listarConStockBajo() {
        return insumoRepository.findInsumosConStockBajo()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    private Insumo obtenerInsumo(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Insumo no encontrado"));
    }

    private InsumoResponse mapearResponse(Insumo insumo) {
        return InsumoResponse.builder()
                .id(insumo.getId())
                .nombre(insumo.getNombre())
                .stockActual(insumo.getStockActual())
                .stockMinimo(insumo.getStockMinimo())
                .unidadMedida(insumo.getUnidadMedida())
                .stockBajo(insumo.getStockActual().compareTo(insumo.getStockMinimo()) <= 0)
                .build();
    }
}
