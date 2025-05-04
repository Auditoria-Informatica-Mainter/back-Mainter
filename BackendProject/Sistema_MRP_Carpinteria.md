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
  "direccion": "Av. Principal #123",
  "telefono": "555-123-4567",
  "email": "contacto@maderasdelsur.com",
  "personaContacto": "Juan Pérez",
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
    "direccion": "Av. Principal #123",
    "telefono": "555-123-4567",
    "email": "contacto@maderasdelsur.com",
    "personaContacto": "Juan Pérez",
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
      "direccion": "Av. Principal #123",
      "telefono": "555-123-4567",
      "email": "contacto@maderasdelsur.com",
      "personaContacto": "Juan Pérez",
      "activo": true
    },
    {
      "id": 2,
      "nombre": "Ferretería Central",
      "ruc": "20987654321",
      "direccion": "Jr. Comercio #789",
      "telefono": "555-987-6543",
      "email": "info@ferreteriacentral.com",
      "personaContacto": "María López",
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
    "direccion": "Av. Principal #123",
    "telefono": "555-123-4567",
    "email": "contacto@maderasdelsur.com",
    "personaContacto": "Juan Pérez",
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
      "activo": true
    }
  ]
}
```

#### 5. Obtener Proveedores por Estado
- **Endpoint**: `GET /api/proveedores/estado`
- **Summary**: Lista proveedores por su estado activo/inactivo
- **Funcionalidad**:
  - Permite filtrar proveedores por su estado
  - Facilita la gestión de proveedores activos o inactivos
- **Parámetros**:
  - `activo`: Estado a filtrar (true/false)
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Proveedores con estado activo: true",
  "data": [
    {
      "id": 1,
      "nombre": "Maderas del Sur",
      "ruc": "20123456789",
      "direccion": "Av. Principal #123",
      "telefono": "555-123-4567",
      "email": "contacto@maderasdelsur.com",
      "personaContacto": "Juan Pérez",
      "activo": true
    }
  ]
}
```

#### 6. Actualizar Proveedor
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
    "direccion": "Av. Principal #123",
    "telefono": "555-123-4567",
    "email": "contacto@maderasdelsur.com",
    "personaContacto": "Juan Pérez Mendoza",
    "activo": true
  }
}
```

#### 7. Cambiar Estado del Proveedor
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

#### 8. Eliminar Proveedor
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

#### 9. Obtener Proveedor con Materiales
- **Endpoint**: `GET /api/proveedores/{id}/con-materiales`
- **Summary**: Obtiene un proveedor con sus materiales asociados
- **Funcionalidad**:
  - Retorna todos los datos del proveedor
  - Incluye la lista de materiales suministrados con sus detalles
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Proveedor con materiales encontrado",
  "data": {
    "id": 1,
    "nombre": "Maderas del Sur",
    "ruc": "20123456789",
    "direccion": "Av. Principal #123",
    "telefono": "555-123-4567",
    "email": "contacto@maderasdelsur.com",
    "personaContacto": "Juan Pérez",
    "activo": true,
    "materiales": [
      {
        "id": 1,
        "nombre": "Madera de Pino",
        "unidadMedida": "m²",
        "precio": 25.5,
        "stockActual": 100,
        "precioProveedor": 50.5,
        "cantidadMinima": 10
      },
      {
        "id": 3,
        "nombre": "Madera de Cedro",
        "unidadMedida": "m²",
        "precio": 75.0,
        "stockActual": 50,
        "precioProveedor": 75.0,
        "cantidadMinima": 5
      }
    ]
  }
}
```

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

#### 7. Materiales que Necesitan Reabastecimiento
- **Endpoint**: `GET /api/materiales/necesitan-reabastecimiento`
- **Summary**: Lista los materiales que necesitan reponerse según punto de reorden
- **Funcionalidad**:
  - Identifica materiales cuyo stock está por debajo del punto de reorden
  - Facilita la planificación anticipada de compras
- **Response**: Similar a "Materiales con Stock Bajo"

#### 8. Obtener Materiales por Proveedor
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

#### 9. Actualizar Material
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

#### 10. Actualizar Imagen de Material
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

#### 11. Actualizar Stock de Material
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

#### 12. Eliminar Material
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
  "statusCode": 201,
  "message": "Categoría creada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Maderas",
    "descripcion": "Tipos de maderas para carpintería",
    "subcategoria": {
      "id": 1,
      "nombre": "Materia Prima"
    }
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
{
  "statusCode": 200,
  "message": "Lista de categorías",
  "data": [
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
}
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
  "statusCode": 200,
  "message": "Categoría encontrada",
  "data": {
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
}
```

#### 4. Obtener Categoría por Nombre
- **Endpoint**: `GET /api/categorias/nombre/{nombre}`
- **Summary**: Busca una categoría por su nombre exacto
- **Funcionalidad**:
  - Permite búsquedas precisas por nombre
  - Facilita la identificación de categorías específicas
- **Response**: Similar a "Obtener Categoría por ID"

#### 5. Obtener Categorías por Subcategoría
- **Endpoint**: `GET /api/categorias/subcategoria/{subCategoriaId}`
- **Summary**: Lista categorías asociadas a una subcategoría específica
- **Funcionalidad**:
  - Permite filtrar categorías por su subcategoría padre
  - Facilita la navegación jerárquica del sistema de clasificación
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Categorías de la subcategoría ID: 1",
  "data": [
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
      "id": 3,
      "nombre": "Herrajes",
      "descripcion": "Herrajes y accesorios metálicos",
      "subcategoria": {
        "id": 1,
        "nombre": "Materia Prima"
      }
    }
  ]
}
```

#### 6. Actualizar Categoría
- **Endpoint**: `PUT /api/categorias/{id}`
- **Summary**: Actualiza información de una categoría existente
- **Funcionalidad**:
  - Permite modificar propiedades de la categoría
  - Valida la existencia de subcategoría en caso de cambio
  - Mantiene integridad referencial con productos y materiales
- **Request**: Similar al de creación
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Categoría actualizada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Maderas Premium",
    "descripcion": "Maderas de alta calidad para carpintería fina",
    "subcategoria": {
      "id": 1,
      "nombre": "Materia Prima"
    }
  }
}
```

#### 7. Eliminar Categoría
- **Endpoint**: `DELETE /api/categorias/{id}`
- **Summary**: Elimina una categoría del sistema
- **Nota**: En la implementación actual, este endpoint está marcado como no implementado
- **Response** (405 Method Not Allowed):
```json
{
  "statusCode": 405,
  "message": "Eliminación de categorías no implementada"
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
   - Actualizar inventario de materiales automáticamente

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
- **Permisos**: ADMIN, PRODUCCION
- **Summary**: Crea un nuevo producto con su lista de materiales (BOM)
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
  "imagen": "url_imagen.jpg"
}
```

#### 4. Buscar Productos
- **Endpoint**: `GET /api/productos/buscar`
- **Summary**: Búsqueda de productos por término en nombre o descripción
- **Funcionalidad**:
  - Permite búsquedas flexibles por coincidencia parcial
  - Busca en nombre, descripción y otros campos relevantes
  - Facilita la exploración del catálogo de productos
- **Parámetros**:
  - `q`: Término de búsqueda
- **Response** (200 OK): Lista de productos que coinciden con el término

#### 5. Actualizar Producto
- **Endpoint**: `PUT /api/productos/{id}`
- **Permisos**: ADMIN, PRODUCCION
- **Summary**: Actualiza información de un producto existente
- **Funcionalidad**:
  - Permite modificar propiedades del producto
  - Actualiza la lista de materiales (BOM)
  - Valida la existencia de categoría y materiales
  - Mantiene la integridad de los datos de stock
- **Request**: Similar al de creación
- **Response** (200 OK): Producto actualizado

#### 6. Actualizar Imagen de Producto
- **Endpoint**: `PUT /api/productos/{id}/imagen`
- **Permisos**: ADMIN, PRODUCCION
- **Summary**: Actualiza solo la URL de la imagen del producto
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

#### 7. Actualizar Stock de Producto
- **Endpoint**: `PUT /api/productos/{id}/stock`
- **Permisos**: ADMIN, ALMACEN, PRODUCCION
- **Summary**: Actualiza el stock de un producto
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
  "stock": 8,
  "stock_minimo": 2,
  "categoria": {
    "id": 2,
    "nombre": "Muebles"
  }
}
```

#### 8. Eliminar Producto
- **Endpoint**: `DELETE /api/productos/{id}`
- **Permisos**: ADMIN
- **Summary**: Elimina un producto del sistema
- **Funcionalidad**:
  - Valida que el producto no esté en uso en pedidos
  - Elimina referencias asociadas, incluyendo la lista de materiales
- **Response** (204 No Content)

#### 9. Obtener Productos con Stock Bajo
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
    "stock": 2,
    "stock_minimo": 2,
    "activo": true,
    "imagen": "url_imagen.jpg",
    "categoria": {
      "id": 2,
      "nombre": "Muebles"
    }
  }
]
```

#### 10. Obtener Materiales de un Producto
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
    "id": 1,
    "material": {
      "id": 1,
      "nombre": "Madera de Pino",
      "tipo_unidad": "m²",
      "stock": 100
    },
    "cantidad": 3
  },
  {
    "id": 2,
    "material": {
      "id": 2,
      "nombre": "Clavos",
      "tipo_unidad": "unidades",
      "stock": 500
    },
    "cantidad": 1
  }
]
```

#### 11. Verificar Disponibilidad de Materiales
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

#### 12. Registrar Producción
- **Endpoint**: `POST /api/productos/{id}/producir`
- **Permisos**: ADMIN, PRODUCCION
- **Summary**: Registra la producción de un producto, actualizando inventario
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

## Gestión de Almacenes y Sectores

### Secuencia de Pasos para la Gestión de Almacenes y Sectores
1. **Configuración Inicial**
   - Autenticarse como ADMIN
   - Definir la estructura física de la empresa

2. **Creación de Almacenes**
   - Registrar almacenes principales con `POST /api/almacenes`
   - Definir capacidades y ubicaciones
   - Establecer parámetros base

3. **Organización de Sectores**
   - Crear sectores específicos con `POST /api/sectores`
   - Asociar cada sector a un almacén
   - Definir tipos y capacidades

4. **Asignación**
   - Asociar materiales a sectores
   - Organizar por tipo y necesidad
   - Configurar ubicaciones lógicas

5. **Mantenimiento**
   - Actualizar información según cambios físicos
   - Adaptar la estructura a nuevas necesidades
   - Gestionar capacidades y ocupación

> **Nota importante**: El sistema está diseñado con una estructura jerárquica donde los almacenes son las entidades principales que contienen sectores. Los sectores, a su vez, pueden contener materiales específicos. Esta organización permite un control preciso de la ubicación física de los materiales dentro de la empresa.

### Endpoints de Almacenes

#### 1. Crear Almacén
- **Endpoint**: `POST /api/almacenes`
- **Summary**: Registra un nuevo almacén en el sistema
- **Funcionalidad**:
  - Valida unicidad del nombre
  - Establece capacidades y parámetros iniciales
  - Crea la estructura base para contener sectores
- **Request**:
```json
{
  "nombre": "Almacén Central",
  "ubicacion": "Bloque A - Planta Principal",
  "capacidad": 1000,
  "descripcion": "Almacén principal para materias primas"
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Almacén creado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Almacén Central",
    "ubicacion": "Bloque A - Planta Principal",
    "capacidad": 1000,
    "descripcion": "Almacén principal para materias primas"
  }
}
```

#### 2. Obtener Todos los Almacenes
- **Endpoint**: `GET /api/almacenes`
- **Summary**: Lista todos los almacenes registrados
- **Funcionalidad**:
  - Retorna la lista completa de almacenes
  - Incluye información básica de cada almacén
  - Facilita la gestión general de espacios
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de almacenes",
  "data": [
    {
      "id": 1,
      "nombre": "Almacén Central",
      "ubicacion": "Bloque A - Planta Principal",
      "capacidad": 1000,
      "descripcion": "Almacén principal para materias primas"
    },
    {
      "id": 2,
      "nombre": "Almacén de Productos Terminados",
      "ubicacion": "Bloque B - Planta Baja",
      "capacidad": 800,
      "descripcion": "Almacenamiento de productos listos para distribución"
    }
  ]
}
```

#### 3. Obtener Almacén por ID
- **Endpoint**: `GET /api/almacenes/{id}`
- **Summary**: Obtiene información detallada de un almacén específico
- **Funcionalidad**:
  - Retorna todos los datos del almacén
  - Facilita la consulta de detalles particulares
  - Base para otras operaciones específicas
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Almacén encontrado",
  "data": {
    "id": 1,
    "nombre": "Almacén Central",
    "ubicacion": "Bloque A - Planta Principal",
    "capacidad": 1000,
    "descripcion": "Almacén principal para materias primas"
  }
}
```

#### 4. Obtener Almacén por Nombre
- **Endpoint**: `GET /api/almacenes/nombre/{nombre}`
- **Summary**: Busca un almacén por su nombre exacto
- **Funcionalidad**:
  - Permite búsquedas precisas por nombre
  - Facilita la identificación de almacenes específicos
- **Response**: Similar a "Obtener Almacén por ID"

#### 5. Obtener Almacenes por Capacidad
- **Endpoint**: `GET /api/almacenes/capacidad/{capacidad}`
- **Summary**: Lista almacenes con capacidad mayor a la especificada
- **Funcionalidad**:
  - Filtra almacenes con capacidad superior a un valor
  - Ayuda en la planificación de distribución de materiales
  - Útil para decisiones logísticas
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Almacenes con capacidad mayor a: 500",
  "data": [
    {
      "id": 1,
      "nombre": "Almacén Central",
      "ubicacion": "Bloque A - Planta Principal",
      "capacidad": 1000,
      "descripcion": "Almacén principal para materias primas"
    },
    {
      "id": 2,
      "nombre": "Almacén de Productos Terminados",
      "ubicacion": "Bloque B - Planta Baja",
      "capacidad": 800,
      "descripcion": "Almacenamiento de productos listos para distribución"
    }
  ]
}
```

#### 6. Actualizar Almacén
- **Endpoint**: `PUT /api/almacenes/{id}`
- **Summary**: Actualiza información de un almacén existente
- **Funcionalidad**:
  - Permite modificar propiedades del almacén
  - Mantiene las relaciones con sectores existentes
  - Valida coherencia de datos
- **Request**: Similar al de creación
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Almacén actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Almacén Central Principal",
    "ubicacion": "Bloque A - Planta Principal",
    "capacidad": 1200,
    "descripcion": "Almacén principal ampliado para materias primas"
  }
}
```

#### 7. Eliminar Almacén
- **Endpoint**: `DELETE /api/almacenes/{id}`
- **Summary**: Elimina un almacén del sistema
- **Funcionalidad**:
  - Valida que no haya sectores asociados activos
  - Elimina referencias asociadas
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Almacén eliminado exitosamente"
}
```

### Endpoints de Sectores

#### 1. Crear Sector
- **Endpoint**: `POST /api/sectores`
- **Summary**: Registra un nuevo sector en el sistema
- **Funcionalidad**:
  - Valida unicidad del nombre
  - Verifica que el almacén asociado exista
  - Establece parámetros de organización
- **Request**:
```json
{
  "nombre": "Sector Maderas",
  "tipo": "Materia Prima",
  "almacenId": 1,
  "capacidad": 200,
  "descripcion": "Sector para almacenamiento de maderas"
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Sector creado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Sector Maderas",
    "tipo": "Materia Prima",
    "capacidad": 200,
    "descripcion": "Sector para almacenamiento de maderas",
    "almacen": {
      "id": 1,
      "nombre": "Almacén Central"
    }
  }
}
```

#### 2. Obtener Todos los Sectores
- **Endpoint**: `GET /api/sectores`
- **Summary**: Lista todos los sectores registrados
- **Funcionalidad**:
  - Retorna la lista completa de sectores
  - Incluye información básica de cada sector
  - Facilita la gestión general de espacios
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de sectores",
  "data": [
    {
      "id": 1,
      "nombre": "Sector Maderas",
      "tipo": "Materia Prima",
      "capacidad": 200,
      "descripcion": "Sector para almacenamiento de maderas",
      "almacen": {
        "id": 1,
        "nombre": "Almacén Central"
      }
    },
    {
      "id": 2,
      "nombre": "Sector Herrajes",
      "tipo": "Materia Prima",
      "capacidad": 100,
      "descripcion": "Sector para almacenamiento de herrajes y accesorios",
      "almacen": {
        "id": 1,
        "nombre": "Almacén Central"
      }
    }
  ]
}
```

#### 3. Obtener Sector por ID
- **Endpoint**: `GET /api/sectores/{id}`
- **Summary**: Obtiene información detallada de un sector específico
- **Funcionalidad**:
  - Retorna todos los datos del sector
  - Incluye referencia al almacén contenedor
  - Base para otras operaciones específicas
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Sector encontrado",
  "data": {
    "id": 1,
    "nombre": "Sector Maderas",
    "tipo": "Materia Prima",
    "capacidad": 200,
    "descripcion": "Sector para almacenamiento de maderas",
    "almacen": {
      "id": 1,
      "nombre": "Almacén Central"
    }
  }
}
```

#### 4. Obtener Sectores por Almacén
- **Endpoint**: `GET /api/sectores/almacen/{almacenId}`
- **Summary**: Lista los sectores asociados a un almacén específico
- **Funcionalidad**:
  - Filtra sectores por su almacén contenedor
  - Facilita la organización jerárquica
  - Ayuda en la gestión de espacios
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Sectores del almacén ID: 1",
  "data": [
    {
      "id": 1,
      "nombre": "Sector Maderas",
      "tipo": "Materia Prima",
      "capacidad": 200,
      "descripcion": "Sector para almacenamiento de maderas"
    },
    {
      "id": 2,
      "nombre": "Sector Herrajes",
      "tipo": "Materia Prima",
      "capacidad": 100,
      "descripcion": "Sector para almacenamiento de herrajes y accesorios"
    }
  ]
}
```

#### 5. Obtener Sectores por Tipo
- **Endpoint**: `GET /api/sectores/tipo/{tipo}`
- **Summary**: Lista sectores de un tipo específico
- **Funcionalidad**:
  - Filtra sectores por su tipo (Materia Prima, Producto Terminado, etc.)
  - Facilita la organización funcional
  - Útil para operaciones específicas por tipo
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Sectores de tipo: Materia Prima",
  "data": [
    {
      "id": 1,
      "nombre": "Sector Maderas",
      "tipo": "Materia Prima",
      "capacidad": 200,
      "descripcion": "Sector para almacenamiento de maderas"
    },
    {
      "id": 2,
      "nombre": "Sector Herrajes",
      "tipo": "Materia Prima",
      "capacidad": 100,
      "descripcion": "Sector para almacenamiento de herrajes y accesorios"
    }
  ]
}
```

#### 6. Obtener Sector con Almacén
- **Endpoint**: `GET /api/sectores/{id}/con-almacen`
- **Summary**: Obtiene un sector con información detallada de su almacén
- **Funcionalidad**:
  - Retorna el sector con datos completos de su almacén contenedor
  - Proporciona una visión integrada de la jerarquía
  - Útil para interfaces de visualización detalladas
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Sector con almacén encontrado",
  "data": {
    "id": 1,
    "nombre": "Sector Maderas",
    "tipo": "Materia Prima",
    "capacidad": 200,
    "descripcion": "Sector para almacenamiento de maderas",
    "almacen": {
      "id": 1,
      "nombre": "Almacén Central",
      "ubicacion": "Bloque A - Planta Principal",
      "capacidad": 1000,
      "descripcion": "Almacén principal para materias primas"
    }
  }
}
```

#### 7. Obtener Todos los Sectores con Almacén
- **Endpoint**: `GET /api/sectores/con-almacen`
- **Summary**: Lista todos los sectores con información completa de sus almacenes
- **Funcionalidad**:
  - Proporciona una vista completa de la estructura física
  - Facilita la visualización y gestión global
  - Incluye todos los detalles relevantes de cada nivel
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de sectores con almacén",
  "data": [
    {
      "id": 1,
      "nombre": "Sector Maderas",
      "tipo": "Materia Prima",
      "capacidad": 200,
      "descripcion": "Sector para almacenamiento de maderas",
      "almacen": {
        "id": 1,
        "nombre": "Almacén Central",
        "ubicacion": "Bloque A - Planta Principal",
        "capacidad": 1000,
        "descripcion": "Almacén principal para materias primas"
      }
    },
    {
      "id": 2,
      "nombre": "Sector Herrajes",
      "tipo": "Materia Prima",
      "capacidad": 100,
      "descripcion": "Sector para almacenamiento de herrajes y accesorios",
      "almacen": {
        "id": 1,
        "nombre": "Almacén Central",
        "ubicacion": "Bloque A - Planta Principal",
        "capacidad": 1000,
        "descripcion": "Almacén principal para materias primas"
      }
    }
  ]
}
```

#### 8. Actualizar Sector
- **Endpoint**: `PUT /api/sectores/{id}`
- **Summary**: Actualiza información de un sector existente
- **Funcionalidad**:
  - Permite modificar propiedades del sector
  - Valida la existencia del almacén en caso de cambio
  - Mantiene la integridad de los datos
- **Request**: Similar al de creación
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Sector actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Sector Maderas Industriales",
    "tipo": "Materia Prima",
    "capacidad": 250,
    "descripcion": "Sector ampliado para almacenamiento de maderas industriales",
    "almacen": {
      "id": 1,
      "nombre": "Almacén Central"
    }
  }
}
```

#### 9. Eliminar Sector
- **Endpoint**: `DELETE /api/sectores/{id}`
- **Summary**: Elimina un sector del sistema
- **Funcionalidad**:
  - Valida que no haya materiales asociados
  - Elimina referencias asociadas
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Sector eliminado exitosamente"
}
```

## Gestión de Pedidos de Compra

### Secuencia de Pasos para la Gestión de Pedidos
1. **Planificación**
   - Autenticarse en el sistema
   - Verificar materiales con bajo stock
   - Identificar proveedores apropiados

2. **Creación de Pedido**
   - Crear pedido base con `POST /api/pedidos`
   - Seleccionar proveedor y establecer datos generales
   - Configurar fecha estimada de recepción

3. **Agregación de Detalles**
   - Agregar materiales al pedido con `POST /api/pedidos-compra`
   - Especificar cantidades y precios
   - Asociar cada material con el pedido base

4. **Seguimiento**
   - Consultar estado del pedido
   - Actualizar estado cuando sea necesario
   - Registrar fecha real de recepción

5. **Recepción**
   - Verificar materiales recibidos
   - Actualizar inventario
   - Cerrar el pedido o registrar pendientes

### Endpoints de Pedidos

#### 1. Crear Pedido
- **Endpoint**: `POST /api/pedidos`
- **Summary**: Registra un nuevo pedido de compra a proveedor
- **Funcionalidad**:
  - Crea la cabecera del pedido
  - Valida la existencia del proveedor
  - Establece el estado inicial del pedido
  - No incluye los detalles del pedido (se agregan después)
- **Request**:
```json
{
  "proveedorId": 1,
  "fechaEstimadaEntrega": "2023-10-15",
  "observaciones": "Pedido urgente de materiales",
  "estado": "PENDIENTE"
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Pedido creado exitosamente",
  "data": {
    "id": 1,
    "fechaPedido": "2023-09-30T14:35:27.899Z",
    "fechaEstimadaEntrega": "2023-10-15",
    "fechaEntrega": null,
    "observaciones": "Pedido urgente de materiales",
    "estado": "PENDIENTE",
    "proveedor": {
      "id": 1,
      "nombre": "Maderas del Sur"
    }
  }
}
```

#### 2. Obtener Todos los Pedidos
- **Endpoint**: `GET /api/pedidos`
- **Summary**: Lista todos los pedidos registrados
- **Funcionalidad**:
  - Retorna la lista completa de pedidos
  - Incluye información básica de cada pedido
  - No incluye los detalles específicos
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de pedidos",
  "data": [
    {
      "id": 1,
      "fechaPedido": "2023-09-30T14:35:27.899Z",
      "fechaEstimadaEntrega": "2023-10-15",
      "fechaEntrega": null,
      "observaciones": "Pedido urgente de materiales",
      "estado": "PENDIENTE",
      "proveedor": {
        "id": 1,
        "nombre": "Maderas del Sur"
      }
    },
    {
      "id": 2,
      "fechaPedido": "2023-09-29T10:15:42.123Z",
      "fechaEstimadaEntrega": "2023-10-10",
      "fechaEntrega": "2023-10-08T14:30:00.000Z",
      "observaciones": "Pedido de herrajes",
      "estado": "ENTREGADO",
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
- **Summary**: Obtiene información detallada de un pedido específico
- **Funcionalidad**:
  - Retorna todos los datos del pedido
  - Incluye información del proveedor
  - No incluye los detalles del pedido (se obtienen con otro endpoint)
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Pedido encontrado",
  "data": {
    "id": 1,
    "fechaPedido": "2023-09-30T14:35:27.899Z",
    "fechaEstimadaEntrega": "2023-10-15",
    "fechaEntrega": null,
    "observaciones": "Pedido urgente de materiales",
    "estado": "PENDIENTE",
    "proveedor": {
      "id": 1,
      "nombre": "Maderas del Sur",
      "ruc": "20123456789",
      "direccion": "Av. Principal #123",
      "telefono": "555-123-4567",
      "email": "contacto@maderasdelsur.com",
      "personaContacto": "Juan Pérez"
    }
  }
}
```

#### 4. Obtener Pedidos por Estado
- **Endpoint**: `GET /api/pedidos/estado/{estado}`
- **Summary**: Lista pedidos que tienen un estado específico
- **Funcionalidad**:
  - Filtra pedidos por su estado (PENDIENTE, EN_TRANSITO, ENTREGADO, CANCELADO)
  - Facilita el seguimiento de pedidos en cada fase
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Pedidos con estado: PENDIENTE",
  "data": [
    {
      "id": 1,
      "fechaPedido": "2023-09-30T14:35:27.899Z",
      "fechaEstimadaEntrega": "2023-10-15",
      "observaciones": "Pedido urgente de materiales",
      "estado": "PENDIENTE",
      "proveedor": {
        "id": 1,
        "nombre": "Maderas del Sur"
      }
    },
    {
      "id": 3,
      "fechaPedido": "2023-10-01T09:22:15.654Z",
      "fechaEstimadaEntrega": "2023-10-20",
      "observaciones": "Materiales para producción mensual",
      "estado": "PENDIENTE",
      "proveedor": {
        "id": 3,
        "nombre": "Suministros Industriales"
      }
    }
  ]
}
```

#### 5. Obtener Pedidos por Proveedor
- **Endpoint**: `GET /api/pedidos/proveedor/{proveedorId}`
- **Summary**: Lista pedidos asociados a un proveedor específico
- **Funcionalidad**:
  - Filtra pedidos por proveedor
  - Facilita el seguimiento de relaciones comerciales
  - Ayuda a evaluar el desempeño del proveedor
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Pedidos del proveedor ID: 1",
  "data": [
    {
      "id": 1,
      "fechaPedido": "2023-09-30T14:35:27.899Z",
      "fechaEstimadaEntrega": "2023-10-15",
      "observaciones": "Pedido urgente de materiales",
      "estado": "PENDIENTE",
      "proveedor": {
        "id": 1,
        "nombre": "Maderas del Sur"
      }
    },
    {
      "id": 4,
      "fechaPedido": "2023-09-15T11:40:33.789Z",
      "fechaEstimadaEntrega": "2023-09-30",
      "fechaEntrega": "2023-09-28T16:45:00.000Z",
      "observaciones": "Pedido mensual regular",
      "estado": "ENTREGADO",
      "proveedor": {
        "id": 1,
        "nombre": "Maderas del Sur"
      }
    }
  ]
}
```

#### 6. Actualizar Pedido
- **Endpoint**: `PUT /api/pedidos/{id}`
- **Summary**: Actualiza información de un pedido existente
- **Funcionalidad**:
  - Permite modificar propiedades del pedido
  - Actualiza estados y fechas
  - Mantiene la integridad de los datos
- **Request**:
```json
{
  "proveedorId": 1,
  "fechaEstimadaEntrega": "2023-10-20",
  "fechaEntrega": "2023-10-18",
  "observaciones": "Pedido urgente de materiales con fecha actualizada",
  "estado": "ENTREGADO"
}
```
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Pedido actualizado exitosamente",
  "data": {
    "id": 1,
    "fechaPedido": "2023-09-30T14:35:27.899Z",
    "fechaEstimadaEntrega": "2023-10-20",
    "fechaEntrega": "2023-10-18T00:00:00.000Z",
    "observaciones": "Pedido urgente de materiales con fecha actualizada",
    "estado": "ENTREGADO",
    "proveedor": {
      "id": 1,
      "nombre": "Maderas del Sur"
    }
  }
}
```

#### 7. Eliminar Pedido
- **Endpoint**: `DELETE /api/pedidos/{id}`
- **Summary**: Elimina un pedido del sistema
- **Funcionalidad**:
  - Verifica que el pedido exista
  - Elimina el pedido y sus detalles asociados
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Pedido eliminado exitosamente"
}
```

### Endpoints de Detalles de Pedidos

#### 1. Crear Detalle de Pedido
- **Endpoint**: `POST /api/pedidos-compra`
- **Summary**: Registra un nuevo detalle de pedido con un material específico
- **Funcionalidad**:
  - Asocia un material a un pedido existente
  - Establece cantidad, precio y condiciones específicas
  - Permite construir pedidos con múltiples materiales
- **Request**:
```json
{
  "pedidoId": 1,
  "materialId": 3,
  "cantidad": 50,
  "precioUnitario": 75.50,
  "observaciones": "Madera de cedro de primera calidad"
}
```
- **Response** (201 Created):
```json
{
  "statusCode": 201,
  "message": "Detalle de pedido de compra creado exitosamente",
  "data": {
    "id": 1,
    "cantidad": 50,
    "precioUnitario": 75.50,
    "observaciones": "Madera de cedro de primera calidad",
    "pedido": {
      "id": 1,
      "estado": "PENDIENTE"
    },
    "material": {
      "id": 3,
      "nombre": "Madera de Cedro"
    }
  }
}
```

#### 2. Obtener Todos los Detalles de Pedidos
- **Endpoint**: `GET /api/pedidos-compra`
- **Summary**: Lista todos los detalles de pedidos registrados
- **Funcionalidad**:
  - Retorna la lista completa de detalles de pedidos
  - Incluye referencias a pedidos y materiales
  - Proporciona vista global de todos los detalles en el sistema
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Lista de detalles de pedidos de compra",
  "data": [
    {
      "id": 1,
      "cantidad": 50,
      "precioUnitario": 75.50,
      "observaciones": "Madera de cedro de primera calidad",
      "pedido": {
        "id": 1,
        "estado": "PENDIENTE"
      },
      "material": {
        "id": 3,
        "nombre": "Madera de Cedro"
      }
    },
    {
      "id": 2,
      "cantidad": 100,
      "precioUnitario": 25.00,
      "observaciones": "Madera de pino tratada",
      "pedido": {
        "id": 1,
        "estado": "PENDIENTE"
      },
      "material": {
        "id": 1,
        "nombre": "Madera de Pino"
      }
    }
  ]
}
```

#### 3. Obtener Detalle de Pedido por ID
- **Endpoint**: `GET /api/pedidos-compra/{id}`
- **Summary**: Obtiene información detallada de un detalle de pedido específico
- **Funcionalidad**:
  - Retorna todos los datos del detalle del pedido
  - Incluye información del pedido y material asociados
  - Proporciona detalles específicos de la línea de pedido
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Detalle de pedido de compra encontrado",
  "data": {
    "id": 1,
    "cantidad": 50,
    "precioUnitario": 75.50,
    "observaciones": "Madera de cedro de primera calidad",
    "pedido": {
      "id": 1,
      "fechaPedido": "2023-09-30T14:35:27.899Z",
      "estado": "PENDIENTE"
    },
    "material": {
      "id": 3,
      "nombre": "Madera de Cedro",
      "unidadMedida": "m²",
      "stockActual": 30,
      "stockMinimo": 20
    }
  }
}
```

#### 4. Obtener Detalles por Pedido
- **Endpoint**: `GET /api/pedidos-compra/pedido/{pedidoId}`
- **Summary**: Lista los detalles asociados a un pedido específico
- **Funcionalidad**:
  - Filtra detalles por ID de pedido
  - Proporciona vista completa de todos los materiales en un pedido
  - Facilita la visualización de pedidos completos
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Detalles del pedido ID: 1",
  "data": [
    {
      "id": 1,
      "cantidad": 50,
      "precioUnitario": 75.50,
      "observaciones": "Madera de cedro de primera calidad",
      "material": {
        "id": 3,
        "nombre": "Madera de Cedro",
        "unidadMedida": "m²"
      }
    },
    {
      "id": 2,
      "cantidad": 100,
      "precioUnitario": 25.00,
      "observaciones": "Madera de pino tratada",
      "material": {
        "id": 1,
        "nombre": "Madera de Pino",
        "unidadMedida": "m²"
      }
    }
  ]
}
```

#### 5. Obtener Detalles por Material
- **Endpoint**: `GET /api/pedidos-compra/material/{materialId}`
- **Summary**: Lista los detalles de pedidos que contienen un material específico
- **Funcionalidad**:
  - Filtra detalles por ID de material
  - Permite seguimiento histórico de pedidos de un material
  - Útil para análisis de consumo y planificación
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Detalles con material ID: 3",
  "data": [
    {
      "id": 1,
      "cantidad": 50,
      "precioUnitario": 75.50,
      "observaciones": "Madera de cedro de primera calidad",
      "pedido": {
        "id": 1,
        "fechaPedido": "2023-09-30T14:35:27.899Z",
        "estado": "PENDIENTE"
      }
    },
    {
      "id": 5,
      "cantidad": 25,
      "precioUnitario": 78.00,
      "observaciones": "Madera de cedro importada",
      "pedido": {
        "id": 4,
        "fechaPedido": "2023-09-15T11:40:33.789Z",
        "estado": "ENTREGADO"
      }
    }
  ]
}
```

#### 6. Actualizar Detalle de Pedido
- **Endpoint**: `PUT /api/pedidos-compra/{id}`
- **Summary**: Actualiza información de un detalle de pedido existente
- **Funcionalidad**:
  - Permite modificar cantidad, precio y observaciones
  - Opcionalmente cambia el material asociado
  - Mantiene la integridad de los datos
- **Request**:
```json
{
  "pedidoId": 1,
  "materialId": 3,
  "cantidad": 60,
  "precioUnitario": 72.50,
  "observaciones": "Madera de cedro de primera calidad actualizada"
}
```
- **Response** (200 OK):
```json
{
  "statusCode": 200,
  "message": "Detalle de pedido de compra actualizado exitosamente",
  "data": {
    "id": 1,
    "cantidad": 60,
    "precioUnitario": 72.50,
    "observaciones": "Madera de cedro de primera calidad actualizada",
    "pedido": {
      "id": 1,
      "estado": "PENDIENTE"
    },
    "material": {
      "id": 3,
      "nombre": "Madera de Cedro"
    }
  }
}
```

#### 7. Eliminar Detalle de Pedido
- **Endpoint**: `DELETE /api/pedidos-compra/{id}`
- **Summary**: Elimina un detalle de pedido del sistema
- **Funcionalidad**:
  - Verifica que el detalle exista
  - Elimina la línea específica sin afectar al pedido principal
  - Permite ajustes en pedidos existentes
- **Response** (204 No Content):
```json
{
  "statusCode": 204,
  "message": "Detalle de pedido de compra eliminado exitosamente"
}
``` 