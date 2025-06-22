# Documentación del Módulo de Devoluciones (Refactorizado)

## 1. Descripción General

Este documento describe la arquitectura y el uso del módulo de devoluciones después de la refactorización. El objetivo principal fue separar la creación de la cabecera de una devolución de la de sus detalles, utilizando una **API jerárquica** que mejora la claridad y la trazabilidad.

El nuevo flujo se basa en dos operaciones distintas y secuenciales:
1.  **Crear la Devolución**: Primero, se crea una entidad `Devolucion` que actúa como contenedor. Esta contiene información general como la fecha, el motivo y el pedido asociado.
2.  **Añadir Detalles a la Devolución**: Una vez creada la devolución, se añaden los detalles (`Detalle_Devolucion`) uno por uno a través de un endpoint anidado. Cada detalle está obligatoriamente vinculado a un `detalle_pedido` específico, garantizando que sabemos exactamente qué producto de qué pedido se está devolviendo.

**Importante**: La lógica de negocio compleja (como la actualización automática de stock) ha sido eliminada de los servicios por ahora. El sistema se centra únicamente en persistir la información de la devolución de forma precisa.

---

## 2. API Endpoints

La funcionalidad se divide en dos controladores, pero los endpoints para los detalles están anidados bajo la devolución correspondiente, siguiendo las mejores prácticas de REST para relaciones jerárquicas.

### 2.1. DevolucionController (`/api/devoluciones`)

Este controlador gestiona las operaciones CRUD para la entidad `Devolucion` (la cabecera).

| Verbo   | Ruta                          | Descripción                                                |
| :------ | :---------------------------- | :--------------------------------------------------------- |
| `GET`   | `/api/devoluciones`           | Obtiene una lista de todas las devoluciones.               |
| `GET`   | `/api/devoluciones/{id}`      | Obtiene una devolución específica por su ID.               |
| `POST`  | `/api/devoluciones`           | Crea una nueva devolución (solo la cabecera).              |
| `PUT`   | `/api/devoluciones/{id}`      | Actualiza los datos de una devolución existente.           |
| `DELETE`| `/api/devoluciones/{id}`      | Elimina una devolución y sus detalles en cascada.          |
| `GET`   | `/api/devoluciones/usuario/{usuarioId}` | Busca devoluciones por ID de usuario.                      |
| `GET`   | `/api/devoluciones/pedido/{pedidoId}`   | Busca devoluciones por ID de pedido.                       |

### 2.2. DetalleDevolucionController (`/api/devoluciones/{devolucionId}/detalles`)

Este controlador gestiona los detalles de una devolución específica. **Nótese que todos los endpoints están anidados bajo una devolución**.

| Verbo   | Ruta                                       | Descripción                                          |
| :------ | :----------------------------------------- | :--------------------------------------------------- |
| `GET`   | `/{devolucionId}/detalles`                 | Obtiene todos los detalles de una devolución.        |
| `GET`   | `/{devolucionId}/detalles/{detalleId}`     | Obtiene un detalle específico de una devolución.     |
| `POST`  | `/{devolucionId}/detalles`                 | Añade un nuevo detalle a una devolución existente.   |
| `DELETE`| `/{devolucionId}/detalles/{detalleId}`     | Elimina un detalle específico de una devolución.     |

---

## 3. Estructura de Datos (DTOs)

### Peticiones (Request)

**Crear Devolución (`DevolucionDTO`)**
```json
{
    "fecha": "2025-06-21T23:30:00",
    "motivo": "string",
    "descripcion": "string",
    "importe_total": 0.0,
    "estado": false,
    "usuario_id": 0,
    "pedido_id": 0
}
```

**Añadir Detalle de Devolución (`DetalleDevolucionDTO`)**
```json
{
    "detallePedidoId": 0,
    "cantidad": 1,
    "motivo_detalle": "string"
}
```

### Respuestas (Response)

**Respuesta de Detalle (`DetalleDevolucionResponseDTO`)**
```json
{
    "id": 0,
    "detallePedidoId": 0,
    "nombreProducto": "string",
    "cantidad": 0,
    "precioUnitario": 0.0,
    "motivo_detalle": "string"
}
```
*(Nota: Las respuestas para `Devolucion` actualmente devuelven la entidad completa. Se recomienda refactorizar para usar un `DevolucionResponseDTO` que incluya una lista de `DetalleDevolucionResponseDTO`)*.

---

## 4. Flujo de Operaciones y Ejemplos

### Paso 1: Crear la Devolución

Primero, se envía una petición para crear la cabecera de la devolución.

**Endpoint:** `POST /api/devoluciones`

**Petición (Request Body):**
```json
{
    "fecha": "2025-06-21T23:30:00",
    "motivo": "Productos dañados en el envío",
    "descripcion": "La caja llegó abierta y uno de los productos está roto.",
    "importe_total": 50.99,
    "estado": false,
    "usuario_id": 1,
    "pedido_id": 4
}
```

**Respuesta (Ejemplo):**
La respuesta incluirá la entidad `Devolucion` completa con su nuevo ID (ej: `id: 25`).

### Paso 2: Añadir Detalles a la Devolución

Con el ID de la devolución (`25`), se procede a añadir cada artículo devuelto como un detalle.

**Endpoint:** `POST /api/devoluciones/25/detalles`

**Petición (Request Body):**
```json
{
    "detallePedidoId": 12,
    "cantidad": 1,
    "motivo_detalle": "Producto dañado"
}
```

**Respuesta (`DetalleDevolucionResponseDTO`):**
```json
{
    "id": 31,
    "detallePedidoId": 12,
    "nombreProducto": "Taza de cerámica",
    "cantidad": 1,
    "precioUnitario": 50.99,
    "motivo_detalle": "Producto dañado"
}
```
De esta forma, la devolución `25` queda asociada a la devolución del producto correspondiente al detalle de pedido `12`.
