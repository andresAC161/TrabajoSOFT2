package com.pezcasesor.controller;

import com.pezcasesor.model.Bitacora;
import com.pezcasesor.repository.BitacoraRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class BitacoraController {

    private final BitacoraRepository bitacoraRepository;

    public BitacoraController(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    @GetMapping
    public ResponseEntity<List<Bitacora>> listar() {
        return ResponseEntity.ok(bitacoraRepository.findAllByOrderByFechaRegistroDesc());
    }
}
