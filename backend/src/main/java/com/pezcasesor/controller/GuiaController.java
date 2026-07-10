package com.pezcasesor.controller;

import com.pezcasesor.model.GuiaRespuestaDTO;
import com.pezcasesor.service.GuiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class GuiaController {

    private final GuiaService guiaService;

    public GuiaController(GuiaService guiaService) {
        this.guiaService = guiaService;
    }

    @GetMapping("/api/guias")
    public ResponseEntity<List<GuiaRespuestaDTO>> listarPorEspecie(@RequestParam String especie) {
        return ResponseEntity.ok(guiaService.listarPorEspecie(especie));
    }

    @GetMapping("/api/lotes/{loteId}/guias-recomendadas")
    public ResponseEntity<List<GuiaRespuestaDTO>> recomendadas(@PathVariable Long loteId) {
        return ResponseEntity.ok(guiaService.recomendarPorLote(loteId));
    }
}
