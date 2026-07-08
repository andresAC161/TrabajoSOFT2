package com.pezcasesor.controller;

import com.pezcasesor.model.RegistroCrecimiento;
import com.pezcasesor.model.RegistroCrecimientoRegistroDTO;
import com.pezcasesor.model.RegistroCrecimientoRespuestaDTO;
import com.pezcasesor.service.RegistroCrecimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lotes/{loteId}/crecimiento")
public class RegistroCrecimientoController {

    private final RegistroCrecimientoService registroCrecimientoService;

    public RegistroCrecimientoController(RegistroCrecimientoService registroCrecimientoService) {
        this.registroCrecimientoService = registroCrecimientoService;
    }

    @PostMapping
    public ResponseEntity<RegistroCrecimientoRespuestaDTO> registrar(
            @PathVariable Long loteId, @RequestBody RegistroCrecimientoRegistroDTO dto) {
        RegistroCrecimiento registro = new RegistroCrecimiento();
        registro.setLoteId(loteId);
        registro.setPesoPromedioG(dto.getPesoPromedioG());
        registro.setTallaCm(dto.getTallaCm());
        registro.setMortalidad(dto.getMortalidad());
        registro.setFechaMuestreo(dto.getFechaMuestreo());
        RegistroCrecimiento guardado = registroCrecimientoService.registrar(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(guardado));
    }

    @GetMapping
    public ResponseEntity<List<RegistroCrecimientoRespuestaDTO>> listar(@PathVariable Long loteId) {
        List<RegistroCrecimientoRespuestaDTO> lista = registroCrecimientoService.listarPorLote(loteId)
            .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    private RegistroCrecimientoRespuestaDTO toDTO(RegistroCrecimiento r) {
        return new RegistroCrecimientoRespuestaDTO(
            r.getId(), r.getLoteId(), r.getPesoPromedioG(),
            r.getTallaCm(), r.getMortalidad(), r.getFechaMuestreo()
        );
    }
}
