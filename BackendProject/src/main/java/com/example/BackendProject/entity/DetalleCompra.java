package com.example.BackendProject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un detalle de compra en el sistema (proxy a DetallePedidoCompra).
 * Esta clase existe para mantener compatibilidad con el diagrama de clases,
 * pero toda la funcionalidad se implementa a través de DetallePedidoCompra.
 */
@Entity
@Table(name = "detalle_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompra {
    @Id
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private DetallePedidoCompra detallePedido;
    
    /**
     * Constructor con DetallePedidoCompra
     */
    public DetalleCompra(DetallePedidoCompra detallePedido) {
        this.detallePedido = detallePedido;
    }
} 