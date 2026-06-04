package com.icore.cibus.reservas.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.icore.cibus.shared.enums.EstadoReserva;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservaResponse {

    private final Long id;
    private final Long clienteId;
    private final String clienteNombre;
    private final Long mesaId;
    private final String mesaNumero;
    private final LocalDate fechaReserva;
    private final LocalTime horaReserva;
    private final Integer numeroPersonas;
    private final EstadoReserva estado;
    private final LocalDateTime creadoEn;
}
