package com.example.BackendProject.controller;

import com.example.BackendProject.entity.Rol;
import com.example.BackendProject.response.ApiResponse;
import com.example.BackendProject.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Rol>>> listarRoles() {
        List<Rol> roles = rolService.listarRoles();
        return new ResponseEntity<>(
                ApiResponse.<List<Rol>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de roles")
                        .data(roles)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Rol>> obtenerRolPorId(@PathVariable Long id) {
        Rol rol = rolService.obtenerRol(id);
        return new ResponseEntity<>(
                ApiResponse.<Rol>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Rol encontrado")
                        .data(rol)
                        .build(),
                HttpStatus.OK
        );
    }

    // Endpoint para obtener un rol específico por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<Rol>> obtenerRolPorNombre(@PathVariable String nombre) {
        Rol rol = rolService.obtenerRolnnombre(nombre);
        return new ResponseEntity<>(
                ApiResponse.<Rol>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Rol encontrado")
                        .data(rol)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Rol>> guardarRol(@RequestParam String nombre, @RequestBody List<String> nombresPermisos) {
        Rol nuevoRol = rolService.guardarRol(nombre, nombresPermisos);
        return new ResponseEntity<>(
                ApiResponse.<Rol>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Rol creado exitosamente")
                        .data(nuevoRol)
                        .build(),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Rol>> modificarRol(@PathVariable Long id, @RequestParam String nombre, @RequestBody List<String> nombresPermisos) {
        Rol rolActualizado = rolService.modificarRol(id, nombre, nombresPermisos);
        return new ResponseEntity<>(
                ApiResponse.<Rol>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Rol actualizado exitosamente")
                        .data(rolActualizado)
                        .build(),
                HttpStatus.OK
        );
    }



    
}
