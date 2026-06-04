package com.icore.cibus.reservas.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.reservas.entity.Reserva;
import com.icore.cibus.shared.enums.EstadoReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByMesaIdAndFechaReservaAndHoraReserva(Long mesaId, LocalDate fechaReserva, LocalTime horaReserva);

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByEstado(EstadoReserva estado);
}
