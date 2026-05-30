package com.pezcasesor.controller;

import com.pezcasesor.model.LoteRegistroDTO;
import com.pezcasesor.model.LoteRespuestaDTO;
import com.pezcasesor.service.LoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @PostMapping
    public ResponseEntity<LoteRespuestaDTO> registrar(@RequestBody LoteRegistroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loteService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<LoteRespuestaDTO>> listar(@RequestParam Long estanqueId) {
        return ResponseEntity.ok(loteService.listarPorEstanque(estanqueId));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<LoteRespuestaDTO> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(loteService.finalizar(id));
    }
}
