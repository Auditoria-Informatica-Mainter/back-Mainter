package com.example.BackendProject.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nombre;
    private String ruc;             // Registro Único de Contribuyente
    
    @ElementCollection
    @CollectionTable(name = "proveedor_direcciones", joinColumns = @JoinColumn(name = "proveedor_id"))
    @Column(name = "direccion")
    private List<String> direcciones = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "proveedor_telefonos", joinColumns = @JoinColumn(name = "proveedor_id"))
    @Column(name = "telefono")
    private List<String> telefonos = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "proveedor_emails", joinColumns = @JoinColumn(name = "proveedor_id"))
    @Column(name = "email")
    private List<String> emails = new ArrayList<>();
    
    private String personaContacto;
    private Boolean activo;         // Indica si el proveedor está activo
    private String ciudad;         // Ciudad donde se ubica el proveedor
    private String pais;           // País donde se ubica el proveedor
    
    @OneToMany(mappedBy = "proveedor")
    private List<ProveedorMaterial> materiales = new ArrayList<>();
    
    @OneToMany(mappedBy = "proveedor")
    private List<Pedido> pedidos = new ArrayList<>();
    
    /**
     * Constructor con parámetros principales para crear un proveedor
     */
    public Proveedor(String nombre, String ruc, List<String> direcciones, List<String> telefonos, 
                    List<String> emails, String personaContacto, String ciudad, String pais) {
        this.nombre = nombre;
        this.ruc = ruc;
        this.direcciones = direcciones;
        this.telefonos = telefonos;
        this.emails = emails;
        this.personaContacto = personaContacto;
        this.ciudad = ciudad;
        this.pais = pais;
        this.activo = true; // Por defecto, el proveedor está activo
    }
}
