package com.example.BackendProject.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private List<ItemCarritoDTO> items;
    private LocalDateTime fechaCreacion;
    private boolean activo;
} 