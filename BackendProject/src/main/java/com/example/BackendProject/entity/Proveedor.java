package com.example.BackendProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un proveedor de materiales en el sistema.
 */
@Entity
@Table(name = "proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedor_id_seq")
    @SequenceGenerator(name = "proveedor_id_seq", sequenceName = "proveedor_id_seq", allocationSize = 1)
    private Long id;
    
    private String nombre;
    private String ruc;             // Registro Único de Contribuyente
    
    private String direccion;
    private String telefono;
    private String email;
    
    private String personaContacto;
    private Boolean activo;         // Indica si el proveedor está activo

    @OneToMany(mappedBy = "proveedor")
    @JsonManagedReference
    private List<ProveedorMaterial> materiales = new ArrayList<>();
    
    @OneToMany(mappedBy = "proveedor")
    @JsonBackReference
    private List<Compra> compras = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales para crear un proveedor
     */
    public Proveedor(String nombre, String ruc, String direccion, String telefono, 
                    String email, String personaContacto) {
        this.nombre = nombre;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.personaContacto = personaContacto;

        this.activo = true; // Por defecto, el proveedor está activo
    }
}
