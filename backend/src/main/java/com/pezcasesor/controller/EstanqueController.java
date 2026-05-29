package com.pezcasesor.controller;

import com.pezcasesor.model.EstanqueRegistroDTO;
import com.pezcasesor.model.EstanqueRespuestaDTO;
import com.pezcasesor.service.EstanqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estanques")
public class EstanqueController {

    private final EstanqueService estanqueService;

    public EstanqueController(EstanqueService estanqueService) {
        this.estanqueService = estanqueService;
    }

    @PostMapping
    public ResponseEntity<EstanqueRespuestaDTO> registrar(@RequestBody EstanqueRegistroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estanqueService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<EstanqueRespuestaDTO>> listar(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(estanqueService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstanqueRespuestaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(estanqueService.buscarPorId(id));
    }
}
