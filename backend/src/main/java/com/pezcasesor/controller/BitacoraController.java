package com.pezcasesor.controller;

import com.pezcasesor.model.BitacoraRespuestaDTO;
import com.pezcasesor.service.BitacoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class BitacoraController {

    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @GetMapping
    public ResponseEntity<List<BitacoraRespuestaDTO>> listar() {
        return ResponseEntity.ok(bitacoraService.listar());
    }
}
