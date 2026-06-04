package com.icore.cibus.clientes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icore.cibus.clientes.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuarioId(Long usuarioId);

    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
