package com.example.BackendProject.service;

import com.example.BackendProject.dto.MetodoPagoDTO;
import com.example.BackendProject.entity.Metodo_pago;
import com.example.BackendProject.repository.MetodoPagoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    public List<Metodo_pago> listarMetodosPago() {
        return metodoPagoRepository.findAll();
    }

    public Metodo_pago obtenerMetodoPago(Long id) {
        return metodoPagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));
    }

    public Metodo_pago crearMetodoPago(MetodoPagoDTO dto) {
        Metodo_pago metodo = Metodo_pago.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
        return metodoPagoRepository.save(metodo);
    }

    public Metodo_pago actualizarMetodoPago(Long id, MetodoPagoDTO dto) {
        Metodo_pago existente = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());

        return metodoPagoRepository.save(existente);
    }

    public void eliminarMetodoPago(Long id) {
        Metodo_pago existente = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));

        metodoPagoRepository.delete(existente);
    }

    public Metodo_pago buscarPorNombre(String nombre) {
        return metodoPagoRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado con nombre: " + nombre));
    }
}
