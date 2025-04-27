# Checklist de Implementación - Sistema MRP Carpintería

Este documento de seguimiento muestra el progreso en la implementación de los módulos de Gestión de Inventario, Productos, Materiales y Proveedores para el sistema MRP de la carpintería.

## Entidades

### Gestión de Materiales
- [x] Entidad Material
- [x] DTO de Material
- [x] Repository de Material

### Gestión de Productos
- [x] Entidad Producto
- [x] Entidad ProductoMaterial (BOM)
- [x] DTOs de Producto
- [x] Repository de Producto
- [x] Repository de ProductoMaterial

### Gestión de Proveedores
- [x] Entidad Proveedor
- [x] Entidad ProveedorMaterial
- [x] DTOs de Proveedor
- [x] Repository de Proveedor
- [x] Repository de ProveedorMaterial

### Gestión de Compras
- [x] Entidad Compra (implementada como Pedido)
- [x] Entidad DetalleCompra (implementada como DetallePedidoCompra)
- [x] DTOs de Compra (implementada como PedidoDTO y DetallePedidoCompraDTO)
- [x] Repository de Compra (implementado como PedidoRepository)
- [x] Repository de DetalleCompra (implementado como DetallePedidoCompraRepository)

### Gestión de Clientes
- [x] Entidad Cliente
- [x] Repository de Cliente

## Servicios

### Gestión de Materiales
- [x] MaterialService
- [x] Métodos CRUD
- [x] Métodos de control de stock
- [x] Método para obtener materiales con stock bajo
- [x] Método para actualizar imagen

### Gestión de Productos
- [x] ProductoService
- [x] Métodos CRUD
- [x] Métodos de control de stock
- [x] Método para obtener productos con stock bajo
- [x] Método para gestionar relación con materiales
- [x] Método para actualizar imagen

### Control de Stock
- [x] StockService (implementado dentro de MaterialService y PedidoService)
- [x] Métodos para verificar disponibilidad
- [x] Métodos para registrar producción
- [x] Métodos para ajustar stock

### Gestión de Proveedores
- [x] ProveedorService
- [x] Métodos CRUD
- [x] Métodos para gestionar relación con materiales

### Gestión de Compras
- [x] CompraService (implementado como PedidoService y DetallePedidoCompraService)
- [x] Métodos para crear pedidos principales
- [x] Métodos para crear detalles de pedidos
- [x] Métodos para actualizar pedidos y detalles
- [x] Métodos para eliminar pedidos y detalles
- [ ] ~~Métodos para registrar recepción~~
- [ ] ~~Métodos para cancelar compras~~

### Gestión de Clientes
- [x] ClienteService
- [x] Métodos CRUD
- [x] Métodos de búsqueda

## Controladores

### Gestión de Materiales
- [x] MaterialController
- [x] Endpoint: Crear Material (POST /api/materiales)
- [x] Endpoint: Obtener todos los Materiales (GET /api/materiales)
- [x] Endpoint: Obtener Material por ID (GET /api/materiales/{id})
- [x] Endpoint: Actualizar Material (PUT /api/materiales/{id})
- [x] Endpoint: Eliminar Material (DELETE /api/materiales/{id})
- [x] Endpoint: Materiales con Stock Bajo (GET /api/materiales/bajo-stock)
- [x] Endpoint: Actualizar Stock de Material (PUT /api/materiales/{id}/stock)
- [x] Endpoint: Actualizar Imagen de Material (PUT /api/materiales/{id}/imagen)

### Gestión de Productos
- [x] ProductoController
- [x] Endpoint: Crear Producto (POST /api/productos)
- [x] Endpoint: Obtener todos los Productos (GET /api/productos)
- [x] Endpoint: Obtener Producto por ID (GET /api/productos/{id})
- [x] Endpoint: Actualizar Producto (PUT /api/productos/{id})
- [x] Endpoint: Eliminar Producto (DELETE /api/productos/{id})
- [x] Endpoint: Productos con Stock Bajo (GET /api/productos/bajo-stock)
- [x] Endpoint: Lista de Materiales de un Producto (GET /api/productos/{id}/materiales)
- [x] Endpoint: Actualizar Imagen de Producto (PUT /api/productos/{id}/imagen)
- [x] Endpoint: Verificar Disponibilidad (GET /api/productos/{id}/verificar-disponibilidad)
- [x] Endpoint: Registrar Producción (POST /api/productos/{id}/producir)

### Control de Stock
- [x] StockController (implementado a través de MaterialController y PedidoController)
- [x] Endpoint: Verificar Disponibilidad de Materiales (GET /api/materiales/stock-bajo)
- [x] Endpoint: Registrar Producción (integrado en servicios)
- [x] Endpoint: Ajustar Stock de Material (PUT /api/materiales/{id}/stock)

### Gestión de Proveedores
- [x] ProveedorController
- [x] Endpoint: Crear Proveedor (POST /api/proveedores)
- [x] Endpoint: Obtener todos los Proveedores (GET /api/proveedores)
- [x] Endpoint: Obtener Proveedor por ID (GET /api/proveedores/{id})
- [x] Endpoint: Actualizar Proveedor (PUT /api/proveedores/{id})
- [x] Endpoint: Eliminar Proveedor (DELETE /api/proveedores/{id})
- [x] Endpoint: Buscar Proveedores por nombre (GET /api/proveedores/buscar)
- [x] Endpoint: Obtener Proveedores por país (GET /api/proveedores/pais/{pais})
- [x] Endpoint: Obtener Proveedores por ciudad (GET /api/proveedores/ciudad/{ciudad})

### Gestión de Compras
- [x] PedidoController (implementado para pedidos principales)
- [x] DetallePedidoCompraController (implementado para detalles de pedido)
- [x] Endpoint: Crear Pedido Principal (POST /api/pedidos)
- [x] Endpoint: Obtener todos los Pedidos (GET /api/pedidos)
- [x] Endpoint: Obtener Pedido por ID (GET /api/pedidos/{id})
- [x] Endpoint: Actualizar Pedido (PUT /api/pedidos/{id})
- [x] Endpoint: Eliminar Pedido (DELETE /api/pedidos/{id})
- [x] Endpoint: Buscar Pedidos por estado (GET /api/pedidos/estado/{estado})
- [x] Endpoint: Buscar Pedidos por proveedor (GET /api/pedidos/proveedor/{proveedorId})
- [x] Endpoint: Crear Detalle de Pedido (POST /api/pedidos-compra)
- [x] Endpoint: Obtener todos los Detalles (GET /api/pedidos-compra)
- [x] Endpoint: Obtener Detalle por ID (GET /api/pedidos-compra/{id})
- [x] Endpoint: Obtener Detalles por Pedido (GET /api/pedidos-compra/pedido/{pedidoId})
- [x] Endpoint: Obtener Detalles por Material (GET /api/pedidos-compra/material/{materialId})
- [x] Endpoint: Actualizar Detalle de Pedido (PUT /api/pedidos-compra/{id})
- [x] Endpoint: Eliminar Detalle de Pedido (DELETE /api/pedidos-compra/{id})
- [ ] ~~Endpoint: Actualizar Estado de Compra (PATCH /api/pedidos/{id}/estado)~~
- [ ] ~~Endpoint: Registrar Recepción de Compra (manejo a través del estado)~~
- [ ] ~~Endpoint: Cancelar Compra (a través de cambio de estado)~~

### Gestión de Clientes
- [x] ClienteController
- [x] Endpoint: Crear Cliente (POST /api/clientes)
- [x] Endpoint: Obtener todos los Clientes (GET /api/clientes)
- [x] Endpoint: Obtener Cliente por ID (GET /api/clientes/{id})
- [x] Endpoint: Actualizar Cliente (PUT /api/clientes/{id})
- [x] Endpoint: Eliminar Cliente (DELETE /api/clientes/{id})

### Gestión de Categorías
- [x] CategoriaController
- [x] Endpoint: Crear Categoría (POST /api/categorias)
- [x] Endpoint: Obtener todas las Categorías (GET /api/categorias)
- [x] Endpoint: Obtener Categoría por ID (GET /api/categorias/{id})
- [x] Endpoint: Actualizar Categoría (PUT /api/categorias/{id})

## Pruebas

### Unitarias
- [ ] Tests de MaterialService
- [ ] Tests de ProductoService
- [ ] Tests de StockService
- [ ] Tests de ProveedorService
- [ ] Tests de CompraService

### Integración
- [ ] Tests de MaterialController
- [ ] Tests de ProductoController
- [ ] Tests de StockController
- [ ] Tests de ProveedorController
- [ ] Tests de PedidoController

## Documentación
- [x] Documentación Swagger para MaterialController
- [x] Documentación Swagger para ProductoController
- [x] Documentación Swagger para ProveedorController
- [x] Documentación Swagger para PedidoController
- [x] Documentación Swagger para ClienteController

## Implementación Completada
- [x] Gestión de Materiales
- [x] Gestión de Productos
- [x] Control de Stock (incorporado en servicios existentes)
- [x] Gestión de Proveedores
- [x] Gestión de Compras (implementada como Pedidos y DetallesPedidoCompra)
- [x] Gestión de Clientes
- [x] Gestión de Categorías

## Notas adicionales
- Se ha mejorado el flujo de creación de pedidos de compra:
  1. Primero se crea un pedido principal con `POST /api/pedidos` asociado a un proveedor y un usuario administrador
  2. Luego se crean los detalles de pedido con `POST /api/pedidos-compra` asociados al pedido principal
  3. Esto evita errores de BadRequest por referencias a pedidos inexistentes
- Las funcionalidades de control de stock están integradas en los servicios de Material y Producto.
- Se añadió la gestión de clientes como módulo complementario.
- Quedan pendientes las pruebas unitarias y de integración.
- La entidad Material requiere ajustes para compatibilidad con el DTO existente.
- El controlador PedidoController implementa operaciones CRUD y búsquedas por estado y proveedor.
- El controlador DetallePedidoCompraController implementa operaciones CRUD y búsquedas por pedido y material. 