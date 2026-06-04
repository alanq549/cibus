package com.icore.cibus.empleados.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.icore.cibus.entregas.entity.Entrega;
import com.icore.cibus.inventario.entity.MovimientoInventario;
import com.icore.cibus.pagos.entity.Pago;
import com.icore.cibus.pedidos.entity.Pedido;
import com.icore.cibus.shared.enums.EstadoEmpleado;
import com.icore.cibus.usuarios.entity.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String apellido;

    @Column(nullable = false, length = 80)
    private String puesto;

    @Column(length = 30)
    private String telefono;

    private LocalDate fechaContratacion;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEmpleado estado = EstadoEmpleado.ACTIVO;

    @Column(nullable = false)
    private LocalDateTime creadoEn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @Builder.Default
    @OneToMany(mappedBy = "empleado")
    private List<Pedido> pedidosRegistrados = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "empleado")
    private List<MovimientoInventario> movimientosInventario = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "empleado")
    private List<Pago> pagosRegistrados = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "repartidor")
    private List<Entrega> entregas = new ArrayList<>();
}
