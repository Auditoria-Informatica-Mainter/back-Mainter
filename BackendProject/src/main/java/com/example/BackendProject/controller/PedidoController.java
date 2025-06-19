package com.example.BackendProject.controller;

import com.example.BackendProject.dto.PedidoDTO;
import com.example.BackendProject.dto.PedidoResponseDTO;
import com.example.BackendProject.dto.ProductoPedidoDTO;
import com.example.BackendProject.entity.Pedido;
import com.example.BackendProject.entity.Detalle_pedido;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.PedidoService;
import com.example.BackendProject.service.DetallePedidoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;    @Operation(summary = "Obtener todos los pedidos")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> obtenerTodosLosPedidos() {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPedidosCompletos();
        return new ResponseEntity<>(
                ApiResponse.<List<PedidoResponseDTO>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de pedidos")
                        .data(pedidos)
                        .build(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Obtener un pedido por ID")    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> obtenerPedidoPorId(@PathVariable Long id) {
        try {
            PedidoResponseDTO pedido = pedidoService.obtenerPedidoCompleto(id);
            return new ResponseEntity<>(
                    ApiResponse.<PedidoResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Pedido encontrado")
                            .data(pedido)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<PedidoResponseDTO>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Pedido no encontrado con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @Operation(summary = "Crear un nuevo pedido")
    @PostMapping
    public ResponseEntity<ApiResponse<Pedido>> crearPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido nuevoPedido = pedidoService.crearPedido(pedidoDTO);
            return new ResponseEntity<>(
                    ApiResponse.<Pedido>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Pedido creado exitosamente")
                            .data(nuevoPedido)
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Pedido>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Operation(summary = "Actualizar un pedido existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Pedido>> actualizarPedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        
        try {
            Pedido pedidoActualizado = pedidoService.actualizarPedido(id, pedidoDTO);
            
            return new ResponseEntity<>(
                    ApiResponse.<Pedido>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Pedido actualizado exitosamente")
                            .data(pedidoActualizado)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {

            return new ResponseEntity<>(
                    ApiResponse.<Pedido>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @Operation(summary = "Eliminar un pedido")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPedido(@PathVariable Long id) {
        try {
            pedidoService.eliminarPedido(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message("Pedido eliminado exitosamente")
                            .build(),
                    HttpStatus.NO_CONTENT
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @Operation(summary = "Obtener pedidos por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<Pedido>>> obtenerPedidosPorEstado(@PathVariable Boolean estado) {
        List<Pedido> pedidos = pedidoService.buscarPorEstado(estado);
        return new ResponseEntity<>(
                ApiResponse.<List<Pedido>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Pedidos con estado: " + estado)
                        .data(pedidos)
                        .build(),
                HttpStatus.OK
        );
    }    @Operation(summary = "Obtener productos de un pedido específico", 
               description = "Obtiene todos los productos asociados a un pedido con información detallada del producto y categoría")
    @GetMapping("/{id}/productos")
    public ResponseEntity<ApiResponse<List<ProductoPedidoDTO>>> obtenerProductosPorPedido(@PathVariable Long id) {
        try {
            // Verificar que el pedido existe
            pedidoService.obtenerPedido(id);
            
            // Obtener los productos con información detallada
            List<ProductoPedidoDTO> productosDetallados = detallePedidoService.obtenerProductosDetalladosPorPedido(id);
            
            return new ResponseEntity<>(
                    ApiResponse.<List<ProductoPedidoDTO>>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Productos del pedido ID: " + id)
                            .data(productosDetallados)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<List<ProductoPedidoDTO>>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message("Pedido no encontrado con ID: " + id)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }    @Operation(summary = "Validar/Aprobar un pedido")
    @PutMapping("/{id}/validar")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> validarPedido(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Boolean estado = request.get("estado");
            if (estado == null) {
                return ResponseEntity.badRequest().body(
                    ApiResponse.<PedidoResponseDTO>builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("El campo 'estado' es requerido")
                            .build()
                );
            }
            
            PedidoResponseDTO pedido = pedidoService.cambiarEstadoPedidoCompleto(id, estado);
            return ResponseEntity.ok(
                    ApiResponse.<PedidoResponseDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Pedido " + (estado ? "validado" : "invalidado") + " exitosamente")
                            .data(pedido)
                            .build()
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<PedidoResponseDTO>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build()
            );
        }
    }

    @Operation(summary = "Cambiar estado de un pedido con query parameter")
    @PostMapping("/{id}/actualizar-estado")
    public ResponseEntity<ApiResponse<Pedido>> actualizarEstadoPedido(@PathVariable Long id, @RequestParam("estado") Boolean estado) {
        try {
            Pedido pedido = pedidoService.cambiarEstadoPedido(id, estado);
            return ResponseEntity.ok(
                    ApiResponse.<Pedido>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Estado del pedido actualizado exitosamente")
                            .data(pedido)
                            .build()
            );
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Pedido>builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getReason())
                            .build()
            );
        }
    }
}
