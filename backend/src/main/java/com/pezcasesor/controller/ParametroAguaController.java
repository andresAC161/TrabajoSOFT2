package com.pezcasesor.controller;

import com.pezcasesor.model.ParametroAguaRegistroDTO;
import com.pezcasesor.model.ParametroAguaRespuestaDTO;
import com.pezcasesor.service.ParametroAguaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parametros-agua")
public class ParametroAguaController {

    private final ParametroAguaService parametroAguaService;

    public ParametroAguaController(ParametroAguaService parametroAguaService) {
        this.parametroAguaService = parametroAguaService;
    }

    @PostMapping
    public ResponseEntity<ParametroAguaRespuestaDTO> registrar(@RequestBody ParametroAguaRegistroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parametroAguaService.registrar(dto));
    }

    @GetMapping("/{estanqueId}")
    public ResponseEntity<List<ParametroAguaRespuestaDTO>> listar(@PathVariable Long estanqueId) {
        return ResponseEntity.ok(parametroAguaService.listarPorEstanque(estanqueId));
    }
}
