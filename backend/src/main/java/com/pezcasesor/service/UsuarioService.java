package com.pezcasesor.service;

import com.pezcasesor.model.LoginDTO;
import com.pezcasesor.model.UsuarioRegistroDTO;
import com.pezcasesor.model.UsuarioRespuestaDTO;
import com.pezcasesor.model.Usuario;
import com.pezcasesor.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final BitacoraService bitacoraService;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder,
                           BitacoraService bitacoraService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.bitacoraService = bitacoraService;
    }

    public UsuarioRespuestaDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
            .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos."));
        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasenaHash())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        }
        return new UsuarioRespuestaDTO(
            usuario.getId(), usuario.getNombre(), usuario.getCorreo(),
            usuario.getRol(), usuario.getUbicacion(), usuario.getFechaRegistro()
        );
    }

    public UsuarioRespuestaDTO registrar(UsuarioRegistroDTO dto) {
        if (dto.getCorreo() == null || !dto.getCorreo().matches("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Formato de correo inválido.");
        }
        if (usuarioRepository.existsByCorreo(dto.getCorreo().toLowerCase())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
        if (dto.getContrasena() == null || dto.getContrasena().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().trim());
        usuario.setCorreo(dto.getCorreo().toLowerCase().trim());
        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasena()));
        usuario.setRol(dto.getRol());
        usuario.setUbicacion(dto.getUbicacion());
        usuario.setFechaRegistro(LocalDateTime.now());
        Usuario guardado = usuarioRepository.save(usuario);
        bitacoraService.registrar(guardado.getId(), "Registró su cuenta de usuario");
        return new UsuarioRespuestaDTO(
            guardado.getId(), guardado.getNombre(), guardado.getCorreo(),
            guardado.getRol(), guardado.getUbicacion(), guardado.getFechaRegistro()
        );
    }
}
