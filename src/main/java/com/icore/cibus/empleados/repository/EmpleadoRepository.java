package com.icore.cibus.empleados.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.empleados.entity.Empleado;
import com.icore.cibus.shared.enums.EstadoEmpleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByUsuarioId(Long usuarioId);

    List<Empleado> findByEstado(EstadoEmpleado estado);
}
