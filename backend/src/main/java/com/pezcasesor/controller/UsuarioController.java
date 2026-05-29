package com.pezcasesor.controller;

import com.pezcasesor.model.LoginDTO;
import com.pezcasesor.model.UsuarioRegistroDTO;
import com.pezcasesor.model.UsuarioRespuestaDTO;
import com.pezcasesor.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioRespuestaDTO> registrar(@RequestBody UsuarioRegistroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioRespuestaDTO> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(usuarioService.login(dto));
    }
}
