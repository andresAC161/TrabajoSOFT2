package com.pezcasesor.service;

import com.pezcasesor.model.Bitacora;
import com.pezcasesor.model.BitacoraRespuestaDTO;
import com.pezcasesor.model.Usuario;
import com.pezcasesor.repository.BitacoraRepository;
import com.pezcasesor.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BitacoraService {

    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;

    public BitacoraService(BitacoraRepository bitacoraRepository, UsuarioRepository usuarioRepository) {
        this.bitacoraRepository = bitacoraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void registrar(Long usuarioId, String accion) {
        Bitacora bitacora = new Bitacora();
        bitacora.setUsuarioId(usuarioId);
        bitacora.setAccion(accion);
        bitacora.setFechaRegistro(LocalDateTime.now());
        bitacoraRepository.save(bitacora);
    }

    public List<BitacoraRespuestaDTO> listar() {
        return bitacoraRepository.findAllByOrderByFechaRegistroDesc()
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private BitacoraRespuestaDTO toDTO(Bitacora b) {
        String nombre = usuarioRepository.findById(b.getUsuarioId())
            .map(Usuario::getNombre)
            .orElse("Usuario eliminado");
        return new BitacoraRespuestaDTO(b.getId(), b.getUsuarioId(), nombre, b.getAccion(), b.getFechaRegistro());
    }
}
