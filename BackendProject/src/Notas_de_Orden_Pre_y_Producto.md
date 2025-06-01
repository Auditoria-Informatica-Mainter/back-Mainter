¡Perfecto! Aquí tienes ejemplos claros y actualizados de cómo consumir los endpoints de `Orden_Producto` y `Orden_PreProducto` desde el frontend, reflejando el uso de DTOs y el nuevo formato de respuesta ApiResponse.

---

## Ejemplo de uso de los endpoints (actualizado)

### 1. Crear una nueva Orden_Producto

**POST** `/api/orden-producto`

**Request:**
```json
{
  "cantidad": 10,
  "descripcion": "Pedido urgente",
  "estado": "pendiente",
  "fecha": "2024-06-07T15:30:00",
  "usuarioId": 1,
  "productoId": 2
}
```

**Response:**
```json
{
  "statusCode": 201,
  "message": "Orden de producto creada exitosamente",
  "data": {
    "id": 1,
    "cantidad": 10,
    "descripcion": "Pedido urgente",
    "estado": "pendiente",
    "fecha": "2024-06-07T15:30:00",
    "usuario": { "id": 1, ... },
    "producto": { "id": 2, ... }
  }
}
```

---

### 2. Obtener todas las Orden_Producto

**GET** `/api/orden-producto`

**Response:**
```json
{
  "statusCode": 200,
  "message": "Lista de ordenes de producto",
  "data": [
    {
      "id": 1,
      "cantidad": 10,
      "descripcion": "Pedido urgente",
      "estado": "pendiente",
      "fecha": "2024-06-07T15:30:00",
      "usuario": { "id": 1, ... },
      "producto": { "id": 2, ... }
    },
    ...
  ]
}
```

---

### 3. Obtener una Orden_Producto por ID

**GET** `/api/orden-producto/1`

**Response:**
```json
{
  "statusCode": 200,
  "message": "Orden de producto encontrada",
  "data": {
    "id": 1,
    "cantidad": 10,
    "descripcion": "Pedido urgente",
    "estado": "pendiente",
    "fecha": "2024-06-07T15:30:00",
    "usuario": { "id": 1, ... },
    "producto": { "id": 2, ... }
  }
}
```

---

### 4. Actualizar una Orden_Producto

**PUT** `/api/orden-producto/1`

**Request:**
```json
{
  "cantidad": 12,
  "descripcion": "Pedido actualizado",
  "estado": "en proceso",
  "fecha": "2024-06-07T16:00:00",
  "usuarioId": 1,
  "productoId": 2
}
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Orden de producto actualizada exitosamente",
  "data": {
    "id": 1,
    "cantidad": 12,
    "descripcion": "Pedido actualizado",
    "estado": "en proceso",
    "fecha": "2024-06-07T16:00:00",
    "usuario": { "id": 1, ... },
    "producto": { "id": 2, ... }
  }
}
```

---

### 5. Eliminar una Orden_Producto

**DELETE** `/api/orden-producto/1`

**Response:**
```json
{
  "statusCode": 204,
  "message": "Orden de producto eliminada"
}
```

---

## Lo mismo aplica para Orden_PreProducto (actualizado)

### Crear una nueva Orden_PreProducto

**POST** `/api/orden-preproducto`

**Request:**
```json
{
  "cantidad": 5,
  "descripcion": "Preproducto para prueba",
  "estado": "pendiente",
  "fecha": "2024-06-07T15:45:00",
  "usuarioId": 1,
  "preProductoId": 3
}
```

**Response:**
```json
{
  "statusCode": 201,
  "message": "Orden de preproducto creada exitosamente",
  "data": {
    "id": 1,
    "cantidad": 5,
    "descripcion": "Preproducto para prueba",
    "estado": "pendiente",
    "fecha": "2024-06-07T15:45:00",
    "usuario": { "id": 1, ... },
    "preProducto": { "id": 3, ... }
  }
}
```

---

### Obtener todas

**GET** `/api/orden-preproducto`

**Response:**
```json
{
  "statusCode": 200,
  "message": "Lista de ordenes de preproducto",
  "data": [
    {
      "id": 1,
      "cantidad": 5,
      "descripcion": "Preproducto para prueba",
      "estado": "pendiente",
      "fecha": "2024-06-07T15:45:00",
      "usuario": { "id": 1, ... },
      "preProducto": { "id": 3, ... }
    },
    ...
  ]
}
```

---

### Obtener una por ID

**GET** `/api/orden-preproducto/1`

**Response:**
```json
{
  "statusCode": 200,
  "message": "Orden de preproducto encontrada",
  "data": {
    "id": 1,
    "cantidad": 5,
    "descripcion": "Preproducto para prueba",
    "estado": "pendiente",
    "fecha": "2024-06-07T15:45:00",
    "usuario": { "id": 1, ... },
    "preProducto": { "id": 3, ... }
  }
}
```

---

### Actualizar

**PUT** `/api/orden-preproducto/1`

**Request:**
```json
{
  "cantidad": 6,
  "descripcion": "Actualización de preproducto",
  "estado": "en proceso",
  "fecha": "2024-06-07T16:10:00",
  "usuarioId": 1,
  "preProductoId": 3
}
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Orden de preproducto actualizada exitosamente",
  "data": {
    "id": 1,
    "cantidad": 6,
    "descripcion": "Actualización de preproducto",
    "estado": "en proceso",
    "fecha": "2024-06-07T16:10:00",
    "usuario": { "id": 1, ... },
    "preProducto": { "id": 3, ... }
  }
}
```

---

### Eliminar

**DELETE** `/api/orden-preproducto/1`

**Response:**
```json
{
  "statusCode": 204,
  "message": "Orden de preproducto eliminada"
}
```

---

## Notas importantes para el frontend

- **La fecha** debe ser enviada por el frontend en formato ISO.
- **usuarioId** y **productoId/preProductoId** deben enviarse como números (IDs).
- Las respuestas ahora siempre tienen el formato `{ statusCode, message, data }` o `{ statusCode, message }`.
- Si usas fetch en JS, recuerda poner el header `Content-Type: application/json`.

