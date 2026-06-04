package com.icore.cibus.inventario.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.icore.cibus.productos.entity.RecetaProducto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "insumos")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal stockActual;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal stockMinimo;

    @Column(nullable = false, length = 30)
    private String unidadMedida;

    @Builder.Default
    @OneToMany(mappedBy = "insumo")
    private List<RecetaProducto> recetas = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "insumo")
    private List<MovimientoInventario> movimientos = new ArrayList<>();
}
