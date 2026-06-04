package com.icore.cibus.auth.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icore.cibus.auth.dto.request.LoginRequest;
import com.icore.cibus.auth.dto.request.RegisterRequest;
import com.icore.cibus.auth.dto.response.AuthResponse;
import com.icore.cibus.auth.entity.Rol;
import com.icore.cibus.auth.repository.RolRepository;
import com.icore.cibus.auth.service.AuthService;
import com.icore.cibus.security.JwtService;
import com.icore.cibus.security.UsuarioPrincipal;
import com.icore.cibus.shared.enums.EstadoUsuario;
import com.icore.cibus.shared.enums.RolNombre;
import com.icore.cibus.shared.exception.ReglaNegocioException;
import com.icore.cibus.usuarios.entity.Usuario;
import com.icore.cibus.usuarios.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );

        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(principal.getCorreo())
                .orElseThrow(() -> new ReglaNegocioException("Credenciales invalidas"));
        usuario.setUltimoAccesoEn(LocalDateTime.now());

        return construirResponse(principal);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String correo = request.getCorreo().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(correo)) {
            throw new ReglaNegocioException("Ya existe un usuario con el correo indicado");
        }

        RolNombre rolNombre = request.getRol() == null ? RolNombre.CLIENTE : request.getRol();
        Rol rol = obtenerOCrearRol(rolNombre);

        Usuario usuario = Usuario.builder()
                .email(correo)
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(rol)
                .estado(EstadoUsuario.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return construirResponse(new UsuarioPrincipal(usuarioGuardado));
    }

    private Rol obtenerOCrearRol(RolNombre rolNombre) {
        return rolRepository.findByNombreIgnoreCase(rolNombre.name())
                .orElseGet(() -> rolRepository.save(Rol.builder()
                        .nombre(rolNombre.name())
                        .descripcion("Rol " + rolNombre.name())
                        .build()));
    }

    private AuthResponse construirResponse(UsuarioPrincipal principal) {
        return AuthResponse.builder()
                .token(jwtService.generarToken(principal))
                .tipo("Bearer")
                .idUsuario(principal.getId())
                .correo(principal.getCorreo())
                .rol(principal.getRol())
                .build();
    }
}
