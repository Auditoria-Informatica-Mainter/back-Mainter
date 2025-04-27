# Sistema MRP para Carpintería - Documentación Completa de API

Este documento detalla todos los endpoints disponibles en el Sistema MRP para la gestión de una carpintería, incluyendo ejemplos de uso, formatos JSON y análisis funcional detallado.

## Tabla de Contenidos

- [Gestión de Pedidos de Compra](#gestión-de-pedidos-de-compra)
- [Gestión de Materiales](#gestión-de-materiales)
- [Gestión de Productos](#gestión-de-productos)
- [Gestión de Clientes](#gestión-de-clientes)
- [Gestión de Categorías](#gestión-de-categorías)
- [Gestión de Proveedores](#gestión-de-proveedores)
- [Gestión de Usuarios](#gestión-de-usuarios)
- [Autenticación y Seguridad](#autenticación-y-seguridad)

## Gestión de Pedidos de Compra

### Secuencia de Pasos para la Gestión de Pedidos de Compra
1. **Inicio de Sesión**
   - Autenticarse en el sistema usando `POST /auth/login`
   - Obtener y guardar el token JWT para las siguientes operaciones

2. **Verificación Previa**
   - Verificar existencia de proveedores usando `GET /api/proveedores/{id}`
   - Verificar disponibilidad de materiales usando `GET /api/materiales/{id}`

3. **Creación del Pedido Principal**
   - Crear el pedido principal con `POST /api/pedidos`
   - Asignar proveedor y usuario administrador
   - Establecer estado inicial
   - Obtener el ID del pedido creado para usarlo en los detalles

4. **Creación de Detalles de Pedido**
   - Crear detalles de pedido con `POST /api/pedidos-compra` usando el ID del pedido principal
   - Registrar información de material, cantidad y precios
   - Repetir para cada material a incluir en el pedido

5. **Seguimiento del Pedido**
   - Monitorear pedidos principales con `GET /api/pedidos/{id}`
   - Consultar detalles de pedido con `GET /api/pedidos-compra/{id}` o `GET /api/pedidos-compra/pedido/{pedidoId}`
   - Actualizar información según sea necesario

### Endpoints de Pedidos Principales

#### 1. Crear Pedido Principal
- **Endpoint**: `POST /api/pedidos`
- **Summary**: Registra un nuevo pedido principal en el sistema
- **Funcionalidad**: 
  - Valida la existencia del proveedor
  - Establece el estado inicial del pedido
  - Crea la entidad base para agregar detalles posteriormente
- **Request**:
```json
{
  "estado": "PENDIENTE",
  "fecha": "2024-04-24T10:00:00",
  "importe_total": 0.0,
  "importe_descuento": 0.0,
  "proveedorId": 1,
  "usuarioId": 2
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Pedido creado exitosamente",
  "data": {
    "id": 1,
    "estado": "PENDIENTE",
    "fecha": "2024-04-24T10:00:00",
    "importe_total": 0.0,
    "importe_descuento": 0.0,
    "proveedor": {
      "id": 1,
      "nombre": "Maderas del Sur"
    },
    "usuario": {
      "id": 2,
      "nombre": "Admin"
    },
    "detalles": []
  }
}
```
- **Errores Posibles**:
  - 400: Proveedor no existe
  - 400: Usuario no existe
  - 400: Datos inválidos en el pedido

#### 2. Obtener Todos los Pedidos
- **Endpoint**: `GET /api/pedidos`
- **Summary**: Lista todos los pedidos principales registrados
- **Funcionalidad**:
  - Retorna la lista completa de pedidos principales
  - Incluye información básica de cada pedido
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de pedidos",
  "data": [
    {
      "id": 1,
      "estado": "PENDIENTE",
      "fecha": "2024-04-24T10:00:00",
      "importe_total": 0.0,
      "importe_descuento": 0.0,
      "proveedor": {
        "id": 1,
        "nombre": "Maderas del Sur"
      }
    },
    {
      "id": 2,
      "estado": "COMPLETADO",
      "fecha": "2024-04-20T10:00:00",
      "importe_total": 500.0,
      "importe_descuento": 0.0,
      "proveedor": {
        "id": 2,
        "nombre": "Ferretería Central"
      }
    }
  ]
}
```

#### 3. Obtener Pedido por ID
- **Endpoint**: `GET /api/pedidos/{id}`
- **Summary**: Obtiene información detallada de un pedido principal específico
- **Funcionalidad**:
  - Retorna todos los datos del pedido, incluyendo su proveedor
  - Incluye lista de detalles asociados al pedido
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Pedido encontrado",
  "data": {
    "id": 1,
    "estado": "PENDIENTE",
    "fecha": "2024-04-24T10:00:00",
    "importe_total": 255.0,
    "importe_descuento": 0.0,
    "proveedor": {
      "id": 1,
      "nombre": "Maderas del Sur",
      "ruc": "20123456789"
    },
    "usuario": {
      "id": 2,
      "nombre": "Admin"
    },
    "detalles": [
      {
        "id": 1,
        "cantidad": 10,
        "precio": 25.50,
        "importe": 255.00,
        "estado": "PENDIENTE",
        "material": {
          "id": 1,
          "nombre": "Madera de Pino"
        }
      }
    ]
  }
}
```

#### 4. Actualizar Pedido 
- **Endpoint**: `PUT /api/pedidos/{id}`
- **Summary**: Actualiza información de un pedido principal existente
- **Funcionalidad**:
  - Permite modificar propiedades del pedido
  - Actualiza estado, importes y proveedor si es necesario
- **Request**: Similar al de creación
- **Response** (200 OK): Pedido actualizado

### Endpoints de Detalles de Pedido

#### 1. Crear Detalle de Pedido de Compra
- **Endpoint**: `POST /api/pedidos-compra`
- **Controller**: `DetallePedidoCompraController`
- **Summary**: Registra un nuevo detalle de pedido de compra asociado a un pedido principal
- **Funcionalidad**: 
  - Valida la existencia del pedido principal y el material
  - Registra cantidad, precio e importes
  - Actualiza los importes totales del pedido principal
- **Request**:
```json
{
  "cantidad": 10,
  "precio": 25.50,
  "importe": 255.00,
  "importe_desc": 255.00,
  "estado": "PENDIENTE",
  "pedidoId": 1,
  "materialId": 1
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Detalle de pedido creado exitosamente",
  "data": {
    "id": 1,
    "cantidad": 10,
    "precio": 25.50,
    "importe": 255.00,
    "importe_desc": 255.00,
    "estado": "PENDIENTE",
    "pedido": {
      "id": 1
    },
    "material": {
      "id": 1,
      "nombre": "Madera de Pino"
    }
  }
}
```

#### 2. Obtener Todos los Detalles de Pedidos de Compra
- **Endpoint**: `GET /api/pedidos-compra`
- **Controller**: `DetallePedidoCompraController`
- **Summary**: Lista todos los detalles de pedidos de compra registrados
- **Funcionalidad**:
  - Retorna la lista completa de detalles de pedidos
  - Incluye información básica de cada detalle
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de pedidos de compra",
  "data": [
    {
      "id": 1,
      "cantidad": 10,
      "precio": 25.50,
      "importe": 255.00,
      "importe_desc": 255.00,
      "estado": "PENDIENTE",
      "pedido": {
        "id": 1
      },
      "material": {
        "id": 1,
        "nombre": "Madera de Pino"
      }
    },
    {
      "id": 2,
      "cantidad": 5,
      "precio": 15.75,
      "importe": 78.75,
      "importe_desc": 78.75,
      "estado": "PENDIENTE",
      "pedido": {
        "id": 1
      },
      "material": {
        "id": 2,
        "nombre": "Clavos"
      }
    }
  ]
}
```

#### 3. Obtener Detalle de Pedido de Compra por ID
- **Endpoint**: `GET /api/pedidos-compra/{id}`
- **Summary**: Obtiene información detallada de un detalle de pedido de compra específico
- **Funcionalidad**:
  - Retorna todos los datos del detalle de pedido
  - Incluye información del pedido y material asociados
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Pedido de compra encontrado",
  "data": {
    "id": 1,
    "cantidad": 10,
    "precio": 25.50,
    "importe": 255.00,
    "importe_desc": 255.00,
    "estado": "PENDIENTE",
    "pedido": {
      "id": 1,
      "fecha": "2024-04-24T10:00:00"
    },
    "material": {
      "id": 1,
      "nombre": "Madera de Pino",
      "tipo_unidad": "m²",
      "stock": 100
    }
  }
}
```
- **Errores Posibles**:
  - 404: Detalle de pedido no encontrado

#### 4. Actualizar Detalle de Pedido de Compra
- **Endpoint**: `PUT /api/pedidos-compra/{id}`
- **Summary**: Actualiza información de un detalle de pedido de compra existente
- **Funcionalidad**:
  - Permite modificar propiedades del detalle de pedido
  - Valida la existencia del pedido y material en caso de cambio
  - Mantiene la integridad de los datos
- **Request**:
```json
{
  "cantidad": 15,
  "precio": 25.50,
  "importe": 382.50,
  "importe_desc": 382.50,
  "estado": "PENDIENTE",
  "pedidoId": 1,
  "materialId": 1
}
```
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Pedido de compra actualizado exitosamente",
  "data": {
    "id": 1,
    "cantidad": 15,
    "precio": 25.50,
    "importe": 382.50,
    "importe_desc": 382.50,
    "estado": "PENDIENTE",
    "pedido": {
      "id": 1
    },
    "material": {
      "id": 1,
      "nombre": "Madera de Pino"
    }
  }
}
```
- **Errores Posibles**:
  - 404: Detalle de pedido no encontrado
  - 400: Pedido no existe
  - 400: Material no existe

#### 5. Eliminar Detalle de Pedido de Compra
- **Endpoint**: `DELETE /api/pedidos-compra/{id}`
- **Summary**: Elimina un detalle de pedido de compra del sistema
- **Funcionalidad**:
  - Elimina el detalle del pedido
  - Puede afectar cálculos totales del pedido principal
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Pedido de compra eliminado exitosamente"
}
```
- **Errores Posibles**:
  - 404: Detalle de pedido no encontrado

#### Estado actual de funcionalidades pendientes
Las siguientes funcionalidades están documentadas pero aún no implementadas:

- Búsqueda por fecha (`GET /api/pedidos-compra/fecha/{fecha}`)
- Búsqueda por proveedor (`GET /api/pedidos-compra/proveedor/{proveedorId}`) 
- Búsqueda por estado (`GET /api/pedidos-compra/estado/{estado}`)
- Actualización de estado (`PATCH /api/pedidos-compra/{id}/estado`)
- Registro de recepción (`PATCH /api/pedidos-compra/{id}/recepcion`)
- Cancelación de pedido (`PATCH /api/pedidos-compra/{id}/cancelar`)

## Gestión de Materiales

### Secuencia de Pasos para la Gestión de Materiales
1. **Configuración Inicial**
   - Autenticarse en el sistema
   - Verificar permisos de ALMACEN o ADMIN

2. **Registro de Materiales**
   - Crear categorías necesarias si no existen
   - Registrar nuevos materiales con `POST /api/materiales`
   - Asignar categorías, sector y establecer stock inicial

3. **Control de Inventario**
   - Monitorear niveles de stock con `GET /api/materiales/bajo-stock`
   - Actualizar stock cuando sea necesario con `PUT /api/materiales/{id}/stock`
   - Registrar movimientos de inventario

4. **Gestión de Proveedores**
   - Asociar materiales con proveedores
   - Registrar precios y cantidades mínimas
   - Mantener información de contacto actualizada

5. **Mantenimiento**
   - Actualizar información de materiales según necesidad
   - Gestionar imágenes y documentación
   - Mantener categorías y clasificaciones

### Endpoints de Materiales

#### 1. Crear Material
- **Endpoint**: `POST /api/materiales`
- **Summary**: Registra un nuevo material en el sistema
- **Funcionalidad**:
  - Valida unicidad del nombre del material
  - Verifica que la categoría exista
  - Establece valores iniciales de stock
  - Asocia el material con su categoría y sector (opcional)
- **Request**:
```json
{
  "nombre": "Madera de Pino",
  "stockActual": 100,
  "unidadMedida": "m²",
  "stockMinimo": 20,
  "categoriaId": 1,
  "sectorId": 2,
  "descripcion": "Madera de pino para proyectos de carpintería",
  "imagen": "url_imagen.jpg"
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Material creado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Madera de Pino",
    "descripcion": "Madera de pino para proyectos de carpintería",
    "unidadMedida": "m²",
    "precio": 0.0,
    "stockActual": 100,
    "stockMinimo": 20,
    "puntoReorden": 25,
    "categoriaText": "Maderas",
    "activo": true,
    "imagen": "url_imagen.jpg",
    "categoria": {
      "id": 1,
      "nombre": "Maderas"
    },
    "sector": {
      "id": 2,
      "nombre": "Carpintería"
    }
  }
}
```

#### 2. Obtener Todos los Materiales
- **Endpoint**: `GET /api/materiales`
- **Summary**: Lista todos los materiales registrados
- **Funcionalidad**:
  - Retorna la lista completa de materiales
  - Incluye información básica de cada material
  - Incluye la categoría y sector asociados
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de materiales",
  "data": [
    {
      "id": 1,
      "nombre": "Madera de Pino",
      "descripcion": "Madera de pino para proyectos de carpintería",
      "unidadMedida": "m²",
      "stockActual": 100,
      "stockMinimo": 20,
      "puntoReorden": 25,
      "categoriaText": "Maderas",
      "activo": true,
      "categoria": {
        "id": 1,
        "nombre": "Maderas"
      },
      "sector": {
        "id": 2,
        "nombre": "Carpintería"
      }
    },
    {
      "id": 2,
      "nombre": "Clavos",
      "descripcion": "Clavos metálicos estándar",
      "unidadMedida": "unidades",
      "stockActual": 500,
      "stockMinimo": 100,
      "puntoReorden": 150,
      "categoriaText": "Ferretería",
      "activo": true,
      "categoria": {
        "id": 2,
        "nombre": "Ferretería"
      },
      "sector": {
        "id": 3,
        "nombre": "General"
      }
    }
  ]
}
```

#### 3. Obtener Material por ID
- **Endpoint**: `GET /api/materiales/{id}`
- **Summary**: Obtiene información detallada de un material específico
- **Funcionalidad**:
  - Retorna todos los datos del material
  - Incluye información de la categoría y sector asociados
  - Muestra el estado actual del stock
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Material encontrado",
  "data": {
    "id": 1,
    "nombre": "Madera de Pino",
    "descripcion": "Madera de pino para proyectos de carpintería",
    "unidadMedida": "m²",
    "precio": 0.0,
    "stockActual": 100,
    "stockMinimo": 20,
    "puntoReorden": 25,
    "categoriaText": "Maderas",
    "activo": true,
    "imagen": "url_imagen.jpg",
    "categoria": {
      "id": 1,
      "nombre": "Maderas"
    },
    "sector": {
      "id": 2,
      "nombre": "Carpintería"
    }
  }
}
```

#### 4. Obtener Material por Nombre
- **Endpoint**: `GET /api/materiales/nombre/{nombre}`
- **Summary**: Busca un material por su nombre exacto
- **Funcionalidad**:
  - Permite búsquedas precisas por nombre
  - Facilita la identificación de materiales específicos
- **Response**: Similar a "Obtener Material por ID"

#### 5. Buscar Materiales por Término
- **Endpoint**: `GET /api/materiales/buscar`
- **Summary**: Búsqueda flexible de materiales por texto parcial
- **Funcionalidad**:
  - Permite búsquedas por coincidencia parcial
  - Busca en nombre, descripción y otros campos relevantes
  - Facilita la exploración del inventario
- **Parámetros**:
  - `q`: Término de búsqueda
- **Response**: Similar a "Obtener Todos los Materiales" pero filtrado por término de búsqueda

#### 6. Materiales con Stock Bajo
- **Endpoint**: `GET /api/materiales/bajo-stock`
- **Summary**: Lista los materiales con stock igual o inferior al mínimo
- **Funcionalidad**:
  - Identifica automáticamente materiales que requieren reposición
  - Consulta la base de datos para encontrar materiales donde stockActual ≤ stockMinimo
  - Retorna la lista completa de entidades Material con nivel bajo
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Materiales con stock bajo",
  "data": [
    {
      "id": 1,
      "nombre": "Madera de Pino",
      "descripcion": "Madera de pino para proyectos de carpintería",
      "unidadMedida": "m²",
      "precio": 25.5,
      "stockActual": 15,
      "stockMinimo": 20,
      "puntoReorden": 25,
      "categoriaText": "Maderas",
      "activo": true,
      "imagen": "url_imagen.jpg",
      "categoria": {
        "id": 1,
        "nombre": "Maderas"
      }
    }
  ]
}
```

#### 7. Obtener Materiales por Proveedor
- **Endpoint**: `GET /api/materiales/proveedor/{proveedorId}`
- **Summary**: Lista los materiales asociados a un proveedor específico
- **Funcionalidad**:
  - Obtiene los materiales que un proveedor puede suministrar
  - Facilita la gestión de compras a proveedores específicos
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Materiales del proveedor ID: 1",
  "data": [
    {
      "id": 1,
      "nombre": "Madera de Pino",
      "descripcion": "Madera de pino para proyectos de carpintería",
      "unidadMedida": "m²",
      "stockActual": 100,
      "stockMinimo": 20,
      "categoria": {
        "id": 1,
        "nombre": "Maderas"
      }
    },
    {
      "id": 3,
      "nombre": "Madera de Cedro",
      "descripcion": "Madera de cedro importada",
      "unidadMedida": "m²",
      "stockActual": 50,
      "stockMinimo": 15,
      "categoria": {
        "id": 1,
        "nombre": "Maderas"
      }
    }
  ]
}
```

#### 8. Actualizar Material
- **Endpoint**: `PUT /api/materiales/{id}`
- **Summary**: Actualiza información de un material existente
- **Funcionalidad**:
  - Permite modificar propiedades del material
  - Valida la existencia de categoría y sector en caso de cambio
  - Mantiene la integridad de los datos de stock
- **Request**:
```json
{
  "nombre": "Madera de Pino Premium",
  "stockActual": 120,
  "unidadMedida": "m²",
  "stockMinimo": 25,
  "categoriaId": 1,
  "sectorId": 2,
  "descripcion": "Madera de pino de alta calidad",
  "imagen": "url_imagen_actualizada.jpg"
}
```
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Material actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Madera de Pino Premium",
    "descripcion": "Madera de pino de alta calidad",
    "unidadMedida": "m²",
    "precio": 25.5,
    "stockActual": 120,
    "stockMinimo": 25,
    "puntoReorden": 30,
    "categoriaText": "Maderas",
    "activo": true,
    "imagen": "url_imagen_actualizada.jpg",
    "categoria": {
      "id": 1,
      "nombre": "Maderas"
    },
    "sector": {
      "id": 2,
      "nombre": "Carpintería"
    }
  }
}
```

#### 9. Actualizar Imagen de Material
- **Endpoint**: `PUT /api/materiales/{id}/imagen`
- **Summary**: Actualiza solo la URL de la imagen del material
- **Funcionalidad**:
  - Permite actualizar solo el atributo de imagen
  - Útil para operaciones puntuales sin modificar el resto de la entidad
- **Request**:
```json
{
  "imagen": "https://example.com/imagen-actualizada.jpg"
}
```
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Imagen de material actualizada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Madera de Pino Premium",
    "descripcion": "Madera de pino de alta calidad",
    "unidadMedida": "m²",
    "precio": 25.5,
    "stockActual": 120,
    "stockMinimo": 25,
    "puntoReorden": 30,
    "categoriaText": "Maderas",
    "activo": true,
    "imagen": "https://example.com/imagen-actualizada.jpg",
    "categoria": {
      "id": 1,
      "nombre": "Maderas"
    }
  }
}
```

#### 10. Actualizar Stock de Material
- **Endpoint**: `PUT /api/materiales/{id}/stock`
- **Summary**: Actualiza el stock de un material
- **Funcionalidad**:
  - Registra movimientos de inventario (entradas/salidas)
  - Verifica que el stock no quede negativo
  - Actualiza el valor de stockActual
- **Parámetros**:
  - `cantidad` (query parameter): Cantidad a modificar (positiva para incrementar, negativa para decrementar)
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Stock de material actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Madera de Pino",
    "descripcion": "Madera de pino para proyectos de carpintería",
    "unidadMedida": "m²",
    "precio": 25.5,
    "stockActual": 150,
    "stockMinimo": 20,
    "puntoReorden": 25,
    "categoriaText": "Maderas",
    "activo": true,
    "imagen": "url_imagen.jpg",
    "categoria": {
      "id": 1,
      "nombre": "Maderas"
    }
  }
}
```

#### 11. Eliminar Material
- **Endpoint**: `DELETE /api/materiales/{id}`
- **Summary**: Elimina un material del sistema
- **Funcionalidad**:
  - Elimina el material de la base de datos
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Material eliminado exitosamente"
}
```

## Gestión de Productos

### Secuencia de Pasos para la Gestión de Productos
1. **Planificación**
   - Autenticarse con permisos de PRODUCCION o ADMIN
   - Verificar disponibilidad de materiales necesarios
   - Planificar lista de materiales (BOM)

2. **Registro de Productos**
   - Crear producto con `POST /api/productos`
   - Definir BOM y requerimientos de materiales
   - Establecer niveles de stock mínimo

3. **Control de Producción**
   - Verificar disponibilidad con `GET /api/productos/{id}/verificar-disponibilidad`
   - Registrar producción usando `POST /api/productos/{id}/producir`
   - Actualizar inventario de materiales

4. **Seguimiento**
   - Monitorear stock con `GET /api/productos/bajo-stock`
   - Gestionar producción según demanda
   - Mantener registros actualizados

5. **Mantenimiento**
   - Actualizar información de productos
   - Gestionar imágenes y documentación
   - Mantener BOM actualizada

### Endpoints de Productos

#### 1. Crear Producto
- **Endpoint**: `POST /api/productos`
- **Summary**: Crea un nuevo producto con su lista de materiales (BOM)
- **Permisos**: ADMIN, PRODUCCION
- **Funcionalidad**:
  - Registra un nuevo producto en el sistema
  - Valida la existencia de la categoría asociada
  - Gestiona la lista de materiales necesarios (BOM)
  - Verifica la existencia de cada material asociado
  - Establece valores iniciales de stock
- **Request**:
```json
{
  "nombre": "Mesa de Comedor",
  "descripcion": "Mesa de comedor para 6 personas",
  "stock": 5,
  "stock_minimo": 2,
  "categoriaId": 2,
  "materiales": [
    {
      "materialId": 1,
      "cantidad": 3
    },
    {
      "materialId": 2,
      "cantidad": 1
    }
  ],
  "imagen": "url_imagen.jpg"
}
```
- **Response** (201 Created):
```json
{
  "id": 1,
  "nombre": "Mesa de Comedor",
  "descripcion": "Mesa de comedor para 6 personas",
  "stock": 5,
  "stock_minimo": 2,
  "categoria": {
    "id": 2,
    "nombre": "Muebles"
  },
  "materiales": [
    {
      "id": 1,
      "material": {
        "id": 1,
        "nombre": "Madera de Pino"
      },
      "cantidad": 3
    },
    {
      "id": 2,
      "material": {
        "id": 2,
        "nombre": "Clavos"
      },
      "cantidad": 1
    }
  ],
  "imagen": "url_imagen.jpg"
}
```

#### 2. Obtener Todos los Productos
- **Endpoint**: `GET /api/productos`
- **Summary**: Lista todos los productos registrados
- **Funcionalidad**:
  - Retorna la lista completa de productos
  - Incluye información básica de cada producto
  - Opcionalmente muestra la categoría asociada
- **Response** (200 OK):
```json
[
  {
    "id": 1,
    "nombre": "Mesa de Comedor",
    "descripcion": "Mesa de comedor para 6 personas",
    "stock": 5,
    "stock_minimo": 2,
    "categoria": {
      "id": 2,
      "nombre": "Muebles"
    }
  },
  {
    "id": 2,
    "nombre": "Silla",
    "descripcion": "Silla de madera",
    "stock": 20,
    "stock_minimo": 5,
    "categoria": {
      "id": 2,
      "nombre": "Muebles"
    }
  }
]
```

#### 3. Obtener Producto por ID
- **Endpoint**: `GET /api/productos/{id}`
- **Summary**: Obtiene información detallada de un producto específico
- **Funcionalidad**:
  - Retorna todos los datos del producto
  - Incluye información de la categoría asociada
  - Muestra la lista completa de materiales necesarios (BOM)
  - Proporciona el estado actual del stock
- **Response** (200 OK):
```json
{
  "id": 1,
  "nombre": "Mesa de Comedor",
  "descripcion": "Mesa de comedor para 6 personas",
  "stock": 5,
  "stock_minimo": 2,
  "categoria": {
    "id": 2,
    "nombre": "Muebles"
  },
  "materiales": [
    {
      "id": 1,
      "material": {
        "id": 1,
        "nombre": "Madera de Pino",
        "tipo_unidad": "m²"
      },
      "cantidad": 3
    },
    {
      "id": 2,
      "material": {
        "id": 2,
        "nombre": "Clavos",
        "tipo_unidad": "unidades"
      },
      "cantidad": 1
    }
  ],
  "imagen": "url_imagen.jpg",
  "estado_stock": "NORMAL",
  "fecha_creacion": "2024-04-10T09:30:00"
}
```

#### 4. Actualizar Producto
- **Endpoint**: `PUT /api/productos/{id}`
- **Summary**: Actualiza información de un producto existente
- **Permisos**: ADMIN, PRODUCCION
- **Funcionalidad**:
  - Permite modificar propiedades del producto
  - Actualiza la lista de materiales (BOM)
  - Valida la existencia de categoría y materiales
  - Mantiene la integridad de los datos de stock
- **Request**: Similar al de creación
- **Response** (200 OK): Producto actualizado

#### 5. Actualizar Imagen de Producto
- **Endpoint**: `PUT /api/productos/{id}/imagen`
- **Summary**: Actualiza solo la URL de la imagen del producto
- **Permisos**: ADMIN, PRODUCCION
- **Funcionalidad**:
  - Permite actualizar solo el atributo de imagen
  - Útil para operaciones puntuales sin modificar el resto de la entidad
- **Request**:
```json
{
  "imagen": "https://example.com/imagen-actualizada.jpg"
}
```
- **Response** (200 OK): Producto con imagen actualizada

#### 6. Actualizar Stock de Producto
- **Endpoint**: `PUT /api/productos/{id}/stock`
- **Summary**: Actualiza el stock de un producto
- **Permisos**: ADMIN, ALMACEN, PRODUCCION
- **Funcionalidad**:
  - Registra movimientos de inventario (entradas/salidas)
  - Verifica que el stock no quede negativo
  - Genera alertas de stock bajo automáticamente
  - Mantiene historial de movimientos
- **Parámetros**:
  - `cantidad`: Cantidad a modificar (positiva para incrementar, negativa para decrementar)
- **Response** (200 OK):
```json
{
  "id": 1,
  "nombre": "Mesa de Comedor",
  "stock_anterior": 5,
  "cantidad_movimiento": 3,
  "stock_actual": 8,
  "estado_stock": "NORMAL"
}
```

#### 7. Eliminar Producto
- **Endpoint**: `DELETE /api/productos/{id}`
- **Summary**: Elimina un producto del sistema
- **Permisos**: ADMIN
- **Funcionalidad**:
  - Valida que el producto no esté en uso en pedidos
  - Elimina referencias asociadas, incluyendo la lista de materiales
- **Response** (204 No Content)

#### 8. Obtener Productos con Stock Bajo
- **Endpoint**: `GET /api/productos/bajo-stock`
- **Summary**: Lista los productos con stock igual o inferior al mínimo
- **Funcionalidad**:
  - Identifica automáticamente productos que requieren producción
  - Consulta la base de datos para encontrar productos donde stockActual ≤ stockMinimo
  - Retorna la lista completa de productos con nivel bajo
- **Response** (200 OK):
```json
[
  {
    "id": 1,
    "nombre": "Mesa de Comedor",
    "descripcion": "Mesa de comedor para 6 personas",
    "stockActual": 2,
    "stockMinimo": 2,
    "codigo": "PROD-1234567890",
    "activo": true,
    "imagen": "url_imagen.jpg",
    "categoria": {
      "id": 2,
      "nombre": "Muebles"
    }
  }
]
```

#### 9. Obtener Materiales de un Producto
- **Endpoint**: `GET /api/productos/{id}/materiales`
- **Summary**: Obtiene la lista de materiales necesarios para un producto
- **Funcionalidad**:
  - Muestra la lista completa de materiales (BOM)
  - Incluye cantidades y unidades de medida
  - Proporciona información para planificación de producción
- **Response** (200 OK):
```json
[
  {
    "material": {
      "id": 1,
      "nombre": "Madera de Pino",
      "tipo_unidad": "m²",
      "stock": 100
    },
    "cantidad": 3,
    "disponible": true
  },
  {
    "material": {
      "id": 2,
      "nombre": "Clavos",
      "tipo_unidad": "unidades",
      "stock": 500
    },
    "cantidad": 1,
    "disponible": true
  }
]
```

#### 10. Verificar Disponibilidad de Materiales
- **Endpoint**: `GET /api/productos/{id}/verificar-disponibilidad`
- **Summary**: Verifica si hay suficientes materiales para producir una cantidad
- **Funcionalidad**:
  - Comprueba la disponibilidad de todos los materiales necesarios
  - Calcula las cantidades necesarias según la lista de materiales
  - Identifica materiales faltantes si los hay
- **Parámetros**:
  - `cantidad`: Cantidad de unidades del producto a verificar
- **Response** (200 OK - Disponible):
```json
{
  "disponible": true,
  "producto": {
    "id": 1,
    "nombre": "Mesa de Comedor"
  },
  "cantidad": 2,
  "materiales": [
    {
      "materialId": 1,
      "nombre": "Madera de Pino",
      "cantidad_necesaria": 6,
      "stock_actual": 100,
      "disponible": true
    },
    {
      "materialId": 2,
      "nombre": "Clavos",
      "cantidad_necesaria": 2,
      "stock_actual": 500,
      "disponible": true
    }
  ]
}
```

#### 11. Registrar Producción
- **Endpoint**: `POST /api/productos/{id}/producir`
- **Summary**: Registra la producción de un producto, actualizando inventario
- **Permisos**: ADMIN, PRODUCCION
- **Funcionalidad**:
  - Verifica disponibilidad de materiales
  - Consume automáticamente los materiales necesarios
  - Actualiza el stock del producto
  - Registra el movimiento en el historial
- **Parámetros**:
  - `cantidad`: Cantidad de unidades a producir
- **Response** (200 OK):
```json
{
  "id": 1,
  "nombre": "Mesa de Comedor",
  "stock_anterior": 5,
  "produccion": 2,
  "stock_actual": 7,
  "fecha_produccion": "2024-04-24T15:30:00",
  "materiales_consumidos": [
    {
      "materialId": 1,
      "nombre": "Madera de Pino",
      "cantidad": 6
    },
    {
      "materialId": 2,
      "nombre": "Clavos",
      "cantidad": 2
    }
  ]
}
```

## Gestión de Clientes

### Secuencia de Pasos para la Gestión de Clientes
1. **Registro Inicial**
   - Autenticarse con permisos adecuados
   - Verificar duplicados por documento
   - Crear cliente con `POST /api/clientes`

2. **Mantenimiento de Datos**
   - Mantener información actualizada
   - Gestionar estado del cliente
   - Registrar preferencias y observaciones

3. **Seguimiento**
   - Monitorear historial de pedidos
   - Analizar patrones de compra
   - Gestionar relación comercial

4. **Gestión de Pedidos**
   - Asociar pedidos al cliente
   - Verificar estado de cuenta
   - Mantener registro de transacciones

5. **Reportes y Análisis**
   - Generar informes de actividad
   - Analizar satisfacción
   - Planificar acciones comerciales

### Endpoints de Clientes

#### 1. Crear Cliente
- **Endpoint**: `POST /api/clientes`
- **Summary**: Registra un nuevo cliente en el sistema
- **Funcionalidad**:
  - Valida unicidad del documento de identidad
  - Verifica formato de email y teléfono
  - Asigna estado activo por defecto
- **Request**:
```json
{
  "nombre": "Juan Pérez",
  "documentoIdentidad": "12345678",
  "direccion": "Av. Principal 123",
  "telefono": "555-1234",
  "email": "juan@email.com",
  "ciudad": "Lima"
}
```
- **Response** (201 Created):
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "documentoIdentidad": "12345678",
  "direccion": "Av. Principal 123",
  "telefono": "555-1234",
  "email": "juan@email.com",
  "ciudad": "Lima",
  "activo": true,
  "fecha_registro": "2024-04-24T10:00:00"
}
```

#### 2. Obtener Todos los Clientes
- **Endpoint**: `GET /api/clientes`
- **Summary**: Lista todos los clientes registrados
- **Funcionalidad**:
  - Retorna la lista completa de clientes
  - Incluye información básica de cada cliente
  - Opcionalmente filtra por estado activo/inactivo
- **Response** (200 OK):
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "documentoIdentidad": "12345678",
    "email": "juan@email.com",
    "telefono": "555-1234",
    "ciudad": "Lima",
    "activo": true
  },
  {
    "id": 2,
    "nombre": "María Gómez",
    "documentoIdentidad": "87654321",
    "email": "maria@email.com",
    "telefono": "555-5678",
    "ciudad": "Arequipa",
    "activo": true
  }
]
```

#### 3. Obtener Cliente por ID
- **Endpoint**: `GET /api/clientes/{id}`
- **Summary**: Obtiene información detallada de un cliente específico
- **Funcionalidad**:
  - Retorna todos los datos del cliente
  - Incluye historial resumido de pedidos
- **Response** (200 OK):
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "documentoIdentidad": "12345678",
  "direccion": "Av. Principal 123",
  "telefono": "555-1234",
  "email": "juan@email.com",
  "ciudad": "Lima",
  "activo": true,
  "fecha_registro": "2024-04-24T10:00:00",
  "pedidos": [
    {
      "id": 1,
      "numeroPedido": "PED-2024-001",
      "fecha": "2024-04-24T10:00:00",
      "estado": "PENDIENTE",
      "total": 1500.00
    }
  ]
}
```

#### 4. Obtener Cliente por Nombre
- **Endpoint**: `GET /api/clientes/nombre/{nombre}`
- **Summary**: Busca un cliente por su nombre exacto
- **Funcionalidad**:
  - Permite búsquedas precisas por nombre
  - Retorna el primer cliente que coincida exactamente
- **Response**: Similar a "Obtener Cliente por ID"

#### 5. Buscar Clientes por Término
- **Endpoint**: `GET /api/clientes/buscar`
- **Summary**: Búsqueda flexible de clientes por texto parcial
- **Funcionalidad**:
  - Permite búsquedas por coincidencia parcial
  - Busca en nombre, documento, email y otros campos relevantes
  - Facilita la exploración de la base de clientes
- **Parámetros**:
  - `texto`: Término de búsqueda
- **Response**: Arreglo de clientes que coinciden con el término de búsqueda

#### 6. Obtener Cliente por Documento de Identidad
- **Endpoint**: `GET /api/clientes/documento/{documentoIdentidad}`
- **Summary**: Busca un cliente por su documento de identidad
- **Funcionalidad**:
  - Permite búsquedas precisas por documento
  - Facilita la identificación rápida de clientes
- **Response**: Similar a "Obtener Cliente por ID"

#### 7. Obtener Clientes por Estado
- **Endpoint**: `GET /api/clientes/estado`
- **Summary**: Filtra clientes por su estado activo/inactivo
- **Funcionalidad**:
  - Permite filtrar la lista de clientes según su estado
  - Facilita la gestión de clientes activos o inactivos
- **Parámetros**:
  - `activo`: Estado a filtrar (true/false)
- **Response**: Arreglo de clientes filtrados por estado

#### 8. Obtener Clientes por Ciudad
- **Endpoint**: `GET /api/clientes/ciudad/{ciudad}`
- **Summary**: Lista clientes de una ciudad específica
- **Funcionalidad**:
  - Permite segmentar clientes por ubicación geográfica
  - Facilita análisis regionales y campañas localizadas
- **Response**: Arreglo de clientes de la ciudad especificada

#### 9. Actualizar Cliente
- **Endpoint**: `PUT /api/clientes/{id}`
- **Summary**: Actualiza información de un cliente existente
- **Funcionalidad**:
  - Permite modificar los datos del cliente
  - Valida formato de email y teléfono
  - Mantiene historial de cambios
- **Request**: Similar al de creación
- **Response** (200 OK): Cliente actualizado

#### 10. Cambiar Estado de Cliente
- **Endpoint**: `PATCH /api/clientes/{id}/estado`
- **Summary**: Activa o desactiva un cliente
- **Funcionalidad**:
  - Permite cambiar el estado activo/inactivo
  - Facilita la gestión de clientes sin eliminarlos
- **Parámetros**:
  - `activo`: Nuevo estado (true/false)
- **Response** (200 OK): Cliente con estado actualizado

#### 11. Eliminar Cliente
- **Endpoint**: `DELETE /api/clientes/{id}`
- **Summary**: Elimina un cliente del sistema
- **Funcionalidad**:
  - Valida que el cliente no tenga pedidos asociados
  - Elimina el cliente de la base de datos
- **Response** (204 No Content)

## Gestión de Categorías

### Secuencia de Pasos para la Gestión de Categorías
1. **Estructura Inicial**
   - Autenticarse como ADMIN
   - **Crear subcategorías base primero** con `POST /api/subcategorias`
   - Las subcategorías son fundamentales y deben crearse antes que las categorías
   - Establecer jerarquías principales

2. **Configuración**
   - Crear categorías con `POST /api/categorias`
   - Asociar a las subcategorías previamente creadas
   - Las categorías son principalmente para clasificar materias primas comprables (tornillos, madera, herramientas, visagras, etc.)
   - Definir relaciones jerárquicas

3. **Organización**
   - Clasificar materiales considerados como materia prima para la producción
   - Mantener estructura coherente
   - Gestionar relaciones

4. **Mantenimiento**
   - Actualizar categorías según necesidad
   - Reclasificar elementos
   - Mantener documentación

> **Nota importante**: El sistema requiere que primero se creen las subcategorías, para luego poder asociarlas a las categorías. Las categorías están diseñadas específicamente para clasificar materiales considerados como materia prima comprable (tornillos, madera, herramientas, visagras, etc.), es decir, materiales utilizados en la producción.

### Endpoints de Categorías

#### 1. Crear Categoría
- **Endpoint**: `POST /api/categorias`
- **Summary**: Registra una nueva categoría en el sistema
- **Funcionalidad**:
  - Valida unicidad del nombre de categoría
  - Permite asignar una subcategoría (opcional)
  - Establece estructura jerárquica de categorías
- **Request**:
```json
{
  "nombre": "Maderas",
  "descripcion": "Tipos de maderas para carpintería",
  "subCategoriaId": 1
}
```
- **Response** (201 Created):
```json
{
  "id": 1,
  "nombre": "Maderas",
  "descripcion": "Tipos de maderas para carpintería",
  "subcategoria": {
    "id": 1,
    "nombre": "Materia Prima"
  }
}
```

#### 2. Obtener Todas las Categorías
- **Endpoint**: `GET /api/categorias`
- **Summary**: Lista todas las categorías registradas
- **Funcionalidad**:
  - Retorna la lista completa de categorías
  - Incluye información de subcategorías asociadas
  - Facilita la navegación jerárquica
- **Response** (200 OK):
```json
[
  {
    "id": 1,
    "nombre": "Maderas",
    "descripcion": "Tipos de maderas para carpintería",
    "subcategoria": {
      "id": 1,
      "nombre": "Materia Prima"
    }
  },
  {
    "id": 2,
    "nombre": "Muebles",
    "descripcion": "Productos terminados",
    "subcategoria": {
      "id": 2,
      "nombre": "Producto Final"
    }
  }
]
```

#### 3. Obtener Categoría por ID
- **Endpoint**: `GET /api/categorias/{id}`
- **Summary**: Obtiene información detallada de una categoría específica
- **Funcionalidad**:
  - Retorna todos los datos de la categoría
  - Incluye información de subcategoría asociada
  - Opcionalmente lista productos o materiales en esa categoría
- **Response** (200 OK):
```json
{
  "id": 1,
  "nombre": "Maderas",
  "descripcion": "Tipos de maderas para carpintería",
  "subcategoria": {
    "id": 1,
    "nombre": "Materia Prima"
  },
  "materiales": [
    {
      "id": 1,
      "nombre": "Madera de Pino"
    },
    {
      "id": 2,
      "nombre": "Madera de Roble"
    }
  ]
}
```

#### 4. Obtener Categoría por Nombre
- **Endpoint**: `GET /api/categorias/nombre/{nombre}`
- **Summary**: Busca una categoría por su nombre exacto
- **Funcionalidad**:
  - Permite búsquedas precisas por nombre
  - Facilita la identificación de categorías específicas
- **Response**: Similar a "Obtener Categoría por ID"

#### 5. Actualizar Categoría
- **Endpoint**: `PUT /api/categorias/{id}`
- **Summary**: Actualiza información de una categoría existente
- **Funcionalidad**:
  - Permite modificar propiedades de la categoría
  - Valida la existencia de subcategoría en caso de cambio
  - Mantiene integridad referencial con productos y materiales
- **Request**: Similar al de creación
- **Response** (200 OK): Categoría actualizada

## Gestión de Proveedores

### Secuencia de Pasos para la Gestión de Proveedores
1. **Registro Inicial**
   - Autenticarse en el sistema
   - Verificar documentación legal
   - Crear proveedor base con `POST /api/proveedores` (sin materiales asociados inicialmente)

2. **Creación de Materiales**
   - Crear los materiales necesarios con `POST /api/materiales`
   - Los materiales se crean inicialmente sin asociación a proveedores

3. **Asociación de Materiales**
   - Una vez creados los proveedores y materiales, asociarlos utilizando `POST /api/proveedores/{proveedorId}/materiales`
   - Establecer precio, cantidad mínima y descripción de la relación 

4. **Gestión de Relaciones**
   - Mantener información actualizada
   - Consultar materiales de cada proveedor con `GET /api/proveedores/{proveedorId}/materiales`
   - Actualizar relaciones cuando sea necesario

5. **Seguimiento**
   - Evaluar desempeño
   - Monitorear cumplimiento
   - Gestionar incidencias

6. **Mantenimiento**
   - Actualizar información de contacto
   - Gestionar documentación
   - Mantener catálogos actualizados

> **Nota importante**: El sistema está diseñado para que primero se creen los proveedores sin materiales asociados, luego se creen los materiales, y finalmente se establezcan las relaciones entre ambos a través del endpoint específico de asociación. Esta separación permite una gestión más flexible de los proveedores y materiales.

### Endpoints de Proveedores

#### 1. Crear Proveedor
- **Endpoint**: `POST /api/proveedores`
- **Summary**: Registra un nuevo proveedor en el sistema (sin materiales asociados inicialmente)
- **Funcionalidad**:
  - Valida unicidad del RUC o documento de identidad
  - Verifica formato de datos de contacto
  - Crea el proveedor base sin materiales asociados
- **Request**:
```json
{
  "nombre": "Maderas del Sur",
  "ruc": "20123456789",
  "direcciones": ["Av. Principal #123", "Calle Secundaria #456"],
  "telefonos": ["555-123-4567", "999-888-777"],
  "emails": ["contacto@maderasdelsur.com", "ventas@maderasdelsur.com"],
  "personaContacto": "Juan Pérez",
  "ciudad": "Lima",
  "pais": "Perú",
  "activo": true
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Proveedor creado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Maderas del Sur",
    "ruc": "20123456789",
    "direcciones": ["Av. Principal #123", "Calle Secundaria #456"],
    "telefonos": ["555-123-4567", "999-888-777"],
    "emails": ["contacto@maderasdelsur.com", "ventas@maderasdelsur.com"],
    "personaContacto": "Juan Pérez",
    "ciudad": "Lima",
    "pais": "Perú",
    "activo": true
  }
}
```

#### 2. Obtener Todos los Proveedores
- **Endpoint**: `GET /api/proveedores`
- **Summary**: Lista todos los proveedores registrados
- **Funcionalidad**:
  - Retorna la lista completa de proveedores
  - Incluye información básica de cada proveedor
  - Opcionalmente filtra por estado activo/inactivo
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de proveedores",
  "data": [
    {
      "id": 1,
      "nombre": "Maderas del Sur",
      "ruc": "20123456789",
      "direcciones": ["Av. Principal #123"],
      "telefonos": ["555-123-4567"],
      "emails": ["contacto@maderasdelsur.com"],
      "personaContacto": "Juan Pérez",
      "ciudad": "Lima",
      "pais": "Perú",
      "activo": true
    },
    {
      "id": 2,
      "nombre": "Ferretería Central",
      "ruc": "20987654321",
      "direcciones": ["Jr. Comercio #789"],
      "telefonos": ["555-987-6543"],
      "emails": ["info@ferreteriacentral.com"],
      "personaContacto": "María López",
      "ciudad": "Arequipa",
      "pais": "Perú",
      "activo": true
    }
  ]
}
```

#### 3. Obtener Proveedor por ID
- **Endpoint**: `GET /api/proveedores/{id}`
- **Summary**: Obtiene información detallada de un proveedor específico
- **Funcionalidad**:
  - Retorna todos los datos del proveedor
  - No incluye lista de materiales suministrados (se obtienen mediante otro endpoint)
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Proveedor encontrado",
  "data": {
    "id": 1,
    "nombre": "Maderas del Sur",
    "ruc": "20123456789",
    "direcciones": ["Av. Principal #123", "Calle Secundaria #456"],
    "telefonos": ["555-123-4567", "999-888-777"],
    "emails": ["contacto@maderasdelsur.com", "ventas@maderasdelsur.com"],
    "personaContacto": "Juan Pérez",
    "ciudad": "Lima",
    "pais": "Perú",
    "activo": true
  }
}
```

#### 4. Buscar Proveedores
- **Endpoint**: `GET /api/proveedores/buscar`
- **Summary**: Búsqueda flexible de proveedores por texto parcial
- **Funcionalidad**:
  - Permite búsquedas por coincidencia parcial
  - Busca en nombre, RUC, persona de contacto y otros campos relevantes
- **Parámetros**:
  - `texto`: Término de búsqueda
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Resultados de búsqueda para: 'madera'",
  "data": [
    {
      "id": 1,
      "nombre": "Maderas del Sur",
      "ruc": "20123456789",
      "personaContacto": "Juan Pérez",
      "ciudad": "Lima",
      "activo": true
    }
  ]
}
```

#### 5. Obtener Proveedores por Ciudad
- **Endpoint**: `GET /api/proveedores/ciudad/{ciudad}`
- **Summary**: Lista proveedores de una ciudad específica
- **Funcionalidad**:
  - Permite filtrar proveedores por ubicación geográfica
  - Facilita la gestión de proveedores locales
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Proveedores en: Lima",
  "data": [
    {
      "id": 1,
      "nombre": "Maderas del Sur",
      "ruc": "20123456789",
      "personaContacto": "Juan Pérez",
      "ciudad": "Lima",
      "activo": true
    }
  ]
}
```

#### 6. Obtener Proveedores por País
- **Endpoint**: `GET /api/proveedores/pais/{pais}`
- **Summary**: Lista proveedores de un país específico
- **Funcionalidad**:
  - Permite filtrar proveedores por país
  - Facilita la gestión de proveedores nacionales e internacionales
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Proveedores en: Perú",
  "data": [
    {
      "id": 1,
      "nombre": "Maderas del Sur",
      "ruc": "20123456789",
      "ciudad": "Lima",
      "pais": "Perú",
      "activo": true
    },
    {
      "id": 2,
      "nombre": "Ferretería Central",
      "ruc": "20987654321",
      "ciudad": "Arequipa",
      "pais": "Perú",
      "activo": true
    }
  ]
}
```

#### 7. Actualizar Proveedor
- **Endpoint**: `PUT /api/proveedores/{id}`
- **Summary**: Actualiza información de un proveedor existente
- **Funcionalidad**:
  - Permite modificar los datos del proveedor
  - Mantiene las relaciones con materiales existentes
  - Valida formatos de contacto
- **Request**: Similar al de creación
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Proveedor actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Maderas del Sur S.A.C.",
    "ruc": "20123456789",
    "direcciones": ["Av. Principal #123", "Calle Secundaria #456"],
    "telefonos": ["555-123-4567", "999-888-777"],
    "emails": ["contacto@maderasdelsur.com", "ventas@maderasdelsur.com"],
    "personaContacto": "Juan Pérez Mendoza",
    "ciudad": "Lima",
    "pais": "Perú",
    "activo": true
  }
}
```

#### 8. Cambiar Estado del Proveedor
- **Endpoint**: `PATCH /api/proveedores/{id}/estado`
- **Summary**: Activa o desactiva un proveedor
- **Funcionalidad**:
  - Permite cambiar el estado activo/inactivo
  - Facilita la gestión de proveedores sin eliminarlos
- **Parámetros**:
  - `activo`: Nuevo estado (true/false)
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Estado del proveedor actualizado",
  "data": {
    "id": 1,
    "nombre": "Maderas del Sur",
    "activo": false
  }
}
```

#### 9. Eliminar Proveedor
- **Endpoint**: `DELETE /api/proveedores/{id}`
- **Summary**: Elimina un proveedor del sistema
- **Funcionalidad**:
  - Valida que no haya compras asociadas activas
  - Elimina referencias asociadas, incluyendo relaciones con materiales
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Proveedor eliminado exitosamente"
}
```

#### 10. Asociar Material a Proveedor
- **Endpoint**: `POST /api/proveedores/{proveedorId}/materiales`
- **Summary**: Asocia un material existente a un proveedor específico con detalles de suministro
- **Funcionalidad**:
  - Verifica que el proveedor y el material existan
  - Establece la relación proveedor-material con precio, cantidad mínima y descripción
  - Actualiza la disponibilidad de materiales para órdenes de compra
- **Request**:
```json
{
  "materialId": 1,
  "precio": 50.5,
  "cantidadMinima": 10,
  "descripcion": "Madera de pino de alta calidad, certificada"
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Material asociado exitosamente al proveedor",
  "data": {
    "id": 1,
    "proveedor": {
      "id": 1,
      "nombre": "Maderas del Sur"
    },
    "material": {
      "id": 1,
      "nombre": "Madera de Pino"
    },
    "precio": 50.5,
    "cantidadMinima": 10,
    "descripcion": "Madera de pino de alta calidad, certificada"
  }
}
```
- **Nota**: Esta operación debe realizarse después de haber creado tanto el proveedor como el material que se desean asociar.

#### 11. Obtener Materiales de un Proveedor
- **Endpoint**: `GET /api/proveedores/{proveedorId}/materiales`
- **Summary**: Obtiene todos los materiales asociados a un proveedor específico
- **Funcionalidad**:
  - Lista todos los materiales que puede suministrar un proveedor
  - Incluye detalles de precio, cantidad mínima y descripción
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Materiales del proveedor ID: 1",
  "data": [
    {
      "id": 1,
      "proveedor": {
        "id": 1,
        "nombre": "Maderas del Sur"
      },
      "material": {
        "id": 1,
        "nombre": "Madera de Pino",
        "tipo_unidad": "m²"
      },
      "precio": 50.5,
      "cantidadMinima": 10,
      "descripcion": "Madera de pino de alta calidad, certificada"
    },
    {
      "id": 2,
      "proveedor": {
        "id": 1,
        "nombre": "Maderas del Sur"
      },
      "material": {
        "id": 3,
        "nombre": "Madera de Cedro",
        "tipo_unidad": "m²"
      },
      "precio": 75.0,
      "cantidadMinima": 5,
      "descripcion": "Madera de cedro importada, tratada contra plagas"
    }
  ]
}
```

#### 12. Eliminar Asociación entre Proveedor y Material
- **Endpoint**: `DELETE /api/proveedores/{proveedorId}/materiales/{materialId}`
- **Summary**: Elimina la asociación entre un proveedor y un material
- **Funcionalidad**:
  - Verifica que exista la relación entre el proveedor y material
  - Elimina la relación manteniendo intactas ambas entidades
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Relación eliminada exitosamente"
}
```

## Gestión de Usuarios

### Secuencia de Pasos para la Gestión de Usuarios
1. **Configuración Inicial**
   - Autenticarse como ADMIN
   - Planificar estructura de roles
   - Definir políticas de acceso

2. **Creación de Usuarios**
   - Crear usuario con `POST /user`
   - Asignar roles apropiados
   - Establecer credenciales iniciales

3. **Gestión de Accesos**
   - Mantener permisos actualizados
   - Gestionar estados de cuenta
   - Monitorear actividad

4. **Seguridad**
   - Gestionar contraseñas
   - Mantener registros de acceso
   - Aplicar políticas de seguridad

5. **Mantenimiento**
   - Actualizar información de usuarios
   - Gestionar roles y permisos
   - Mantener documentación

### Endpoints de Usuarios

#### 1. Listar Usuarios
- **Endpoint**: `GET /user`
- **Summary**: Lista todos los usuarios registrados
- **Funcionalidad**:
  - Retorna la lista completa de usuarios
  - Permite búsqueda por término
  - Filtra información sensible según permisos
- **Parámetros Opcionales**:
  - `search`: Término de búsqueda
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "OK",
  "data": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@sistema.com",
      "nombreCompleto": "Administrador Sistema",
      "activo": true,
      "roles": [
        {
          "id": 1,
          "nombre": "ADMIN"
        }
      ]
    },
    {
      "id": 2,
      "username": "usuario1",
      "email": "usuario1@sistema.com",
      "nombreCompleto": "Usuario Uno",
      "activo": true,
      "roles": [
        {
          "id": 2,
          "nombre": "PRODUCCION"
        }
      ]
    }
  ]
}
```

#### 2. Obtener Usuario por ID
- **Endpoint**: `GET /user/{id}`
- **Summary**: Obtiene información detallada de un usuario específico
- **Funcionalidad**:
  - Retorna datos del usuario según nivel de acceso
  - Incluye roles asignados
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "OK",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@sistema.com",
    "nombreCompleto": "Administrador Sistema",
    "telefono": "555-1234",
    "fechaCreacion": "2024-01-15T08:30:00",
    "activo": true,
    "roles": [
      {
        "id": 1,
        "nombre": "ADMIN"
      }
    ]
  }
}
```

#### 3. Crear Usuario
- **Endpoint**: `POST /user`
- **Summary**: Registra un nuevo usuario en el sistema
- **Funcionalidad**:
  - Valida unicidad de username y email
  - Cifra la contraseña
  - Asigna roles según permisos
  - Valida formato de datos
- **Request**:
```json
{
  "username": "usuario2",
  "password": "contraseña_segura",
  "email": "usuario2@sistema.com",
  "nombreCompleto": "Usuario Dos",
  "telefono": "555-5678",
  "rolesIds": [2, 3]
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Usuario creado exitosamente",
  "data": {
    "id": 3,
    "username": "usuario2",
    "email": "usuario2@sistema.com",
    "nombreCompleto": "Usuario Dos",
    "activo": true,
    "roles": [
      {
        "id": 2,
        "nombre": "PRODUCCION"
      },
      {
        "id": 3,
        "nombre": "ALMACEN"
      }
    ]
  }
}
```

#### 4. Actualizar Usuario
- **Endpoint**: `PUT /user/{id}`
- **Summary**: Actualiza información de un usuario existente
- **Funcionalidad**:
  - Permite modificar datos del usuario
  - Actualiza roles asignados
  - Valida formato de datos
  - Controla cambios según nivel de permisos
- **Request**: Similar al de creación
- **Response** (200 OK): Usuario actualizado

#### 5. Cambiar Contraseña
- **Endpoint**: `PATCH /user/{id}/password`
- **Summary**: Actualiza la contraseña de un usuario
- **Funcionalidad**:
  - Verifica contraseña actual
  - Valida requisitos de seguridad
  - Cifra la nueva contraseña
- **Request**:
```json
{
  "currentPassword": "contraseña_actual",
  "newPassword": "nueva_contraseña_segura",
  "confirmPassword": "nueva_contraseña_segura"
}
```
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Contraseña actualizada exitosamente"
}
```

#### 6. Cambiar Estado de Usuario
- **Endpoint**: `PATCH /user/{id}/estado`
- **Summary**: Activa o desactiva un usuario
- **Funcionalidad**:
  - Permite activar/desactivar usuarios sin eliminarlos
  - Controla acceso al sistema
- **Request**:
```json
{
  "activo": false
}
```
- **Response** (200 OK): Confirmación de cambio de estado

#### 7. Eliminar Usuario
- **Endpoint**: `DELETE /user/{id}`
- **Summary**: Elimina un usuario del sistema
- **Funcionalidad**:
  - Registra eliminación con marcas de auditoría
  - Valida permisos para eliminación
- **Response** (204 No Content)

## Autenticación y Seguridad

### Secuencia de Pasos para la Autenticación
1. **Inicio de Sesión**
   - Validar credenciales con `POST /auth/login`
   - Obtener token JWT
   - Almacenar token seguramente

2. **Gestión de Sesión**
   - Verificar token con `GET /auth/checkToken`
   - Renovar token cuando sea necesario
   - Mantener sesión activa

3. **Seguridad**
   - Proteger endpoints con token
   - Validar permisos por rol
   - Registrar actividad

4. **Cierre**
   - Cerrar sesión con `POST /auth/logout`
   - Limpiar tokens
   - Registrar cierre

### Endpoints de Autenticación

#### 1. Iniciar Sesión
- **Endpoint**: `POST /auth/login`
- **Summary**: Autentica un usuario y genera token JWT
- **Funcionalidad**:
  - Verifica credenciales de usuario
  - Genera token de acceso con tiempo de expiración
  - Registra información de sesión
- **Request**:
```json
{
  "username": "admin",
  "password": "contraseña_admin"
}
```
- **Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@sistema.com",
  "roles": ["ADMIN"]
}
```

#### 2. Verificar Token
- **Endpoint**: `GET /auth/checkToken`
- **Summary**: Verifica la validez del token actual
- **Funcionalidad**:
  - Confirma que el token sea válido y no haya expirado
  - Retorna información básica del usuario
- **Response** (200 OK):
```json
{
  "valid": true,
  "username": "admin",
  "roles": ["ADMIN"]
}
```

#### 3. Cerrar Sesión
- **Endpoint**: `POST /auth/logout`
- **Summary**: Cierra la sesión actual del usuario
- **Funcionalidad**:
  - Invalida el token actual
  - Registra cierre de sesión
- **Response** (200 OK):
```json
{
  "message": "Sesión cerrada exitosamente"
}
```

## Notas sobre implementación

### Notas sobre formatos de respuesta
Es importante señalar que la implementación actual presenta inconsistencias en los patrones de respuesta:

1. **Controladores con ApiResponse**: Algunos controladores (como `PedidoController` y `DetallePedidoCompraController`) envuelven sus respuestas en un objeto `ApiResponse` con la siguiente estructura:
```json
{
  "statusCode": 200,
  "message": "Mensaje descriptivo",
  "data": { /* datos de la respuesta */ }
}
```

2. **Controladores con respuesta directa**: Otros controladores (como `MaterialController`) devuelven directamente los objetos o listas sin el envoltorio `ApiResponse`.

Estas inconsistencias están presentes en la implementación actual y deben tenerse en cuenta al consumir la API. Los ejemplos en esta documentación reflejan el comportamiento real de cada endpoint.

### Parámetros de API
Para mayor claridad, en toda la documentación se especifica explícitamente el tipo de cada parámetro:

- **path variable**: Parámetro incluido en la URL (ejemplo: `/api/materiales/{id}`)
- **query parameter**: Parámetro añadido a la URL después de `?` (ejemplo: `/api/materiales/{id}/stock?cantidad=10`)
- **request body**: Datos enviados en el cuerpo de la petición

Algunos endpoints pueden combinar diferentes tipos de parámetros.

## Notas Importantes

1. **Autenticación**: Todos los endpoints (excepto login) requieren token JWT en el header:
```
Authorization: Bearer <token>
```

2. **Permisos**:
- Los endpoints están protegidos según roles:
  - ADMIN: Acceso completo
  - PRODUCCION: Gestión de productos y producción
  - ALMACEN: Gestión de inventario y materiales
  - VENTAS: Gestión de pedidos y clientes

3. **Códigos de Estado**:
- 200: Operación exitosa
- 201: Recurso creado
- 400: Error en la solicitud
- 401: No autorizado
- 403: Acceso prohibido
- 404: Recurso no encontrado
- 500: Error interno del servidor

4. **Validaciones Comunes**:
- Campos requeridos
- Formatos de fecha (ISO 8601)
- Valores numéricos positivos
- Unicidad de códigos y documentos
- Integridad referencial 