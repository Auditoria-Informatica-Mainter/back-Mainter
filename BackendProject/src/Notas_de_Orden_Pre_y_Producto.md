¡Perfecto! Aquí tienes ejemplos claros de cómo consumir los endpoints de `Orden_Producto` y `Orden_PreProducto` desde el frontend (por ejemplo, usando fetch en JavaScript o herramientas como Postman).

---

## Ejemplo de uso de los endpoints

### 1. Crear una nueva Orden_Producto

**POST** `/api/orden-producto`

```json
{
  "cantidad": 10,
  "descripcion": "Pedido urgente",
  "estado": "pendiente",
  "fecha": "2024-06-07T15:30:00", // Fecha del dispositivo que la crea
  "usuario": { "id": 1 },
  "producto": { "id": 2 }
}
```

---

### 2. Obtener todas las Orden_Producto

**GET** `/api/orden-producto`

**Respuesta:**
```json
[
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
```

---

### 3. Obtener una Orden_Producto por ID

**GET** `/api/orden-producto/1`

**Respuesta:**
```json
{
  "id": 1,
  "cantidad": 10,
  "descripcion": "Pedido urgente",
  "estado": "pendiente",
  "fecha": "2024-06-07T15:30:00",
  "usuario": { "id": 1, ... },
  "producto": { "id": 2, ... }
}
```

---

### 4. Actualizar una Orden_Producto

**PUT** `/api/orden-producto/1`

```json
{
  "cantidad": 12,
  "descripcion": "Pedido actualizado",
  "estado": "en proceso",
  "fecha": "2024-06-07T16:00:00",
  "usuario": { "id": 1 },
  "producto": { "id": 2 }
}
```

---

### 5. Eliminar una Orden_Producto

**DELETE** `/api/orden-producto/1`

**Respuesta:**  
Código HTTP 204 (No Content)

---

## Lo mismo aplica para Orden_PreProducto

### Crear una nueva Orden_PreProducto

**POST** `/api/orden-preproducto`

```json
{
  "cantidad": 5,
  "descripcion": "Preproducto para prueba",
  "estado": "pendiente",
  "fecha": "2024-06-07T15:45:00",
  "usuario": { "id": 1 },
  "preProducto": { "id": 3 }
}
```

---

### Obtener todas

**GET** `/api/orden-preproducto`

---

### Obtener una por ID

**GET** `/api/orden-preproducto/1`

---

### Actualizar

**PUT** `/api/orden-preproducto/1`

```json
{
  "cantidad": 6,
  "descripcion": "Actualización de preproducto",
  "estado": "en proceso",
  "fecha": "2024-06-07T16:10:00",
  "usuario": { "id": 1 },
  "preProducto": { "id": 3 }
}
```

---

### Eliminar

**DELETE** `/api/orden-preproducto/1`

---

## Notas importantes para el frontend

- **La fecha** debe ser enviada por el frontend en formato ISO.
- **usuario** y **producto/preProducto** deben enviarse como objetos con al menos el campo `id`.
- Si usas fetch en JS, recuerda poner el header `Content-Type: application/json`.

