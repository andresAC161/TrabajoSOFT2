package com.pezcasesor.controller;

import com.pezcasesor.model.NotificacionRespuestaDTO;
import com.pezcasesor.service.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<NotificacionRespuestaDTO>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId));
    }

    @PatchMapping("/{id}/leida")
    public ResponseEntity<NotificacionRespuestaDTO> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarLeida(id));
    }
}
