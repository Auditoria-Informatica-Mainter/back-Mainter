package com.example.BackendProject.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import com.example.BackendProject.entity.Metodo_pago;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByEstado(Boolean estado);
    List<Pedido> findByFechaBetween(Date desde, Date hasta);
    @Query("SELECT p FROM Pedido p WHERE p.metodo_pago.id = :metodoPagoId")
    List<Pedido> findByMetodo_pagoId(Long metodoPagoId);
    List<Pedido> findByUsuario_Id(Long usuarioId);
    List<Pedido> findByUsuario(Usuario usuario);
    
    // Métodos con fetch join para cargar las relaciones
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.metodo_pago LEFT JOIN FETCH p.usuario")
    List<Pedido> findAllWithRelations();
    
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.metodo_pago LEFT JOIN FETCH p.usuario WHERE p.id = :id")
    Optional<Pedido> findByIdWithRelations(@Param("id") Long id);
}
