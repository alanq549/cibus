package com.icore.cibus.reservas.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.clientes.entity.Cliente;
import com.icore.cibus.clientes.repository.ClienteRepository;
import com.icore.cibus.reservas.dto.request.ActualizarReservaRequest;
import com.icore.cibus.reservas.dto.request.CrearReservaRequest;
import com.icore.cibus.reservas.dto.response.ReservaResponse;
import com.icore.cibus.reservas.entity.Mesa;
import com.icore.cibus.reservas.entity.Reserva;
import com.icore.cibus.reservas.repository.MesaRepository;
import com.icore.cibus.reservas.repository.ReservaRepository;
import com.icore.cibus.reservas.service.ReservaService;
import com.icore.cibus.shared.enums.EstadoReserva;
import com.icore.cibus.shared.exception.EntidadNoEncontradaException;
import com.icore.cibus.shared.exception.ReglaNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;

    @Override
    @Transactional
    public ReservaResponse crear(CrearReservaRequest request) {
        Cliente cliente = obtenerCliente(request.getClienteId());
        Mesa mesa = obtenerMesa(request.getMesaId());
        validarMesaDisponibleEnHorario(null, mesa.getId(), request.getFechaReserva(), request.getHoraReserva());

        Reserva reserva = Reserva.builder()
                .cliente(cliente)
                .mesa(mesa)
                .fechaReserva(request.getFechaReserva())
                .horaReserva(request.getHoraReserva())
                .numeroPersonas(request.getNumeroPersonas())
                .estado(EstadoReserva.CONFIRMADA)
                .creadoEn(LocalDateTime.now())
                .build();

        return mapearResponse(reservaRepository.save(reserva));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listar() {
        return reservaRepository.findAll()
                .stream()
                .map(this::mapearResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        return mapearResponse(obtenerReserva(id));
    }

    @Override
    @Transactional
    public ReservaResponse actualizar(Long id, ActualizarReservaRequest request) {
        Reserva reserva = obtenerReserva(id);
        Cliente cliente = obtenerCliente(request.getClienteId());
        Mesa mesa = obtenerMesa(request.getMesaId());
        validarMesaDisponibleEnHorario(id, mesa.getId(), request.getFechaReserva(), request.getHoraReserva());

        reserva.setCliente(cliente);
        reserva.setMesa(mesa);
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setHoraReserva(request.getHoraReserva());
        reserva.setNumeroPersonas(request.getNumeroPersonas());
        reserva.setEstado(request.getEstado());

        return mapearResponse(reserva);
    }

    @Override
    @Transactional
    public ReservaResponse cancelar(Long id) {
        Reserva reserva = obtenerReserva(id);
        reserva.setEstado(EstadoReserva.CANCELADA);
        return mapearResponse(reserva);
    }

    private void validarMesaDisponibleEnHorario(Long reservaActualId, Long mesaId, LocalDate fecha, LocalTime hora) {
        reservaRepository.findByMesaIdAndFechaReservaAndHoraReserva(mesaId, fecha, hora)
                .filter(reserva -> !reserva.getId().equals(reservaActualId))
                .ifPresent(reserva -> {
                    throw new ReglaNegocioException("La mesa ya tiene una reserva en la fecha y hora indicada");
                });
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Cliente no encontrado"));
    }

    private Mesa obtenerMesa(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Mesa no encontrada"));
    }

    private Reserva obtenerReserva(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
    }

    private ReservaResponse mapearResponse(Reserva reserva) {
        return ReservaResponse.builder()
                .id(reserva.getId())
                .clienteId(reserva.getCliente().getId())
                .clienteNombre(reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido())
                .mesaId(reserva.getMesa().getId())
                .mesaNumero(reserva.getMesa().getNumero())
                .fechaReserva(reserva.getFechaReserva())
                .horaReserva(reserva.getHoraReserva())
                .numeroPersonas(reserva.getNumeroPersonas())
                .estado(reserva.getEstado())
                .creadoEn(reserva.getCreadoEn())
                .build();
    }
}
