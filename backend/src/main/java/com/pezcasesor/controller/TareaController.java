package com.pezcasesor.controller;

import com.pezcasesor.model.TareaEditarDTO;
import com.pezcasesor.model.TareaRegistroDTO;
import com.pezcasesor.model.TareaRespuestaDTO;
import com.pezcasesor.service.TareaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping
    public ResponseEntity<TareaRespuestaDTO> crear(@RequestBody TareaRegistroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<TareaRespuestaDTO>> listar(
            @RequestParam Long usuarioId,
            @RequestParam(required = false) String estado) {
        if (estado != null) {
            return ResponseEntity.ok(tareaService.listarPorUsuarioYEstado(usuarioId, estado));
        }
        return ResponseEntity.ok(tareaService.listarPorUsuario(usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TareaRespuestaDTO> actualizar(@PathVariable Long id,
                                                        @RequestBody TareaEditarDTO dto) {
        return ResponseEntity.ok(tareaService.actualizar(id, dto));
    }
}
