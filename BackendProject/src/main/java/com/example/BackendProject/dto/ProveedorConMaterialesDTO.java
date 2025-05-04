package com.example.BackendProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorConMaterialesDTO {
    private Long id;
    private String nombre;
    private String ruc;
    private String direccion;
    private String telefono;
    private String email;
    private String personaContacto;
    private Boolean activo;
    
    // Lista de materiales resumidos
    private List<MaterialResumidoDTO> materiales = new ArrayList<>();
} 