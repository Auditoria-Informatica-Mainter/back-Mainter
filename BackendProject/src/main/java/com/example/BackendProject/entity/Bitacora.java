package com.example.BackendProject.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacoras")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bitacora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String accion;
    private LocalDateTime fecha;
    private String detalles;
    private String direccionIp;
}