package com.example.BackendProject.controller;

import com.example.BackendProject.dto.CarritoDTO;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
@Tag(name = "Carrito", description = "API para gestión del carrito de compras")
public class CarritoController {
    
    @Autowired
    private CarritoService carritoService;
    
    @GetMapping("/{usuarioId}")
    @Operation(
        summary = "Obtener carrito activo",
        description = "Obtiene el carrito activo de un usuario o crea uno nuevo si no existe"
    )
    public ResponseEntity<ApiResponse<CarritoDTO>> obtenerCarrito(@PathVariable Long usuarioId) {
        CarritoDTO carrito = carritoService.obtenerCarritoActivo(usuarioId);
        return ResponseEntity.ok(
            ApiResponse.<CarritoDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Carrito obtenido exitosamente")
                .data(carrito)
                .build()
        );
    }
    
    @PostMapping("/agregar")
    @Operation(
        summary = "Agregar producto al carrito",
        description = "Agrega un producto al carrito o actualiza su cantidad si ya existe"
    )
    public ResponseEntity<ApiResponse<CarritoDTO>> agregarProducto(
            @RequestParam @NotNull Long usuarioId,
            @RequestParam @NotNull Long productoId,
            @RequestParam @NotNull @Min(1) Integer cantidad) {
        CarritoDTO carrito = carritoService.agregarProducto(usuarioId, productoId, cantidad);
        return ResponseEntity.ok(
            ApiResponse.<CarritoDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Producto agregado exitosamente")
                .data(carrito)
                .build()
        );
    }
    
    @PutMapping("/actualizar")
    @Operation(
        summary = "Actualizar cantidad",
        description = "Actualiza la cantidad de un producto en el carrito"
    )
    public ResponseEntity<ApiResponse<CarritoDTO>> actualizarCantidad(
            @RequestParam @NotNull Long usuarioId,
            @RequestParam @NotNull Long productoId,
            @RequestParam @NotNull @Min(1) Integer cantidad) {
        CarritoDTO carrito = carritoService.actualizarCantidad(usuarioId, productoId, cantidad);
        return ResponseEntity.ok(
            ApiResponse.<CarritoDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cantidad actualizada exitosamente")
                .data(carrito)
                .build()
        );
    }
    
    @DeleteMapping("/eliminar")
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del carrito"
    )
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(
            @RequestParam @NotNull Long usuarioId,
            @RequestParam @NotNull Long productoId) {
        carritoService.eliminarProducto(usuarioId, productoId);
        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Producto eliminado exitosamente")
                .build()
        );
    }
    
    @GetMapping("/total")
    @Operation(
        summary = "Obtener total",
        description = "Obtiene el monto total del carrito"
    )
    public ResponseEntity<ApiResponse<Double>> obtenerTotal(@RequestParam @NotNull Long usuarioId) {
        Double total = carritoService.obtenerTotal(usuarioId);
        return ResponseEntity.ok(
            ApiResponse.<Double>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Total obtenido exitosamente")
                .data(total)
                .build()
        );
    }
    
    @PostMapping("/comprar")
    @Operation(
        summary = "Convertir carrito en pedido",
        description = "Convierte el carrito activo en un pedido y lo desactiva"
    )
    public ResponseEntity<ApiResponse<Pedido>> comprarCarrito(
            @RequestParam @NotNull Long usuarioId,
            @RequestParam @NotNull Long metodoPagoId) {
        Pedido pedido = carritoService.convertirCarritoEnPedido(usuarioId, metodoPagoId);
        return ResponseEntity.ok(
            ApiResponse.<Pedido>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Pedido creado exitosamente")
                .data(pedido)
                .build()
        );
    }
} 