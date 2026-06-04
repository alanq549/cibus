package com.icore.cibus.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private final String token;
    private final String tipo;
    private final Long idUsuario;
    private final String correo;
    private final String rol;
}
