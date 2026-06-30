package com.pezcasesor.controller;

import com.pezcasesor.model.Nota;
import com.pezcasesor.model.NotaRegistroDTO;
import com.pezcasesor.model.NotaRespuestaDTO;
import com.pezcasesor.service.NotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lotes/{loteId}/notas")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @PostMapping
    public ResponseEntity<NotaRespuestaDTO> agregar(@PathVariable Long loteId,
                                                     @RequestBody NotaRegistroDTO dto) {
        Nota nota = notaService.agregar(loteId, dto.getContenido());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(nota));
    }

    @GetMapping
    public ResponseEntity<List<NotaRespuestaDTO>> listar(@PathVariable Long loteId) {
        List<NotaRespuestaDTO> notas = notaService.listarPorLote(loteId)
            .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(notas);
    }

    private NotaRespuestaDTO toDTO(Nota n) {
        return new NotaRespuestaDTO(n.getId(), n.getLoteId(), n.getContenido(), n.getFechaHora());
    }
}
