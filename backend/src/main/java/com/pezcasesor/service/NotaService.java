package com.pezcasesor.service;

import com.pezcasesor.model.Nota;
import com.pezcasesor.repository.NotaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public Nota agregar(Long loteId, String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido de la nota es obligatorio.");
        }
        Nota nota = new Nota();
        nota.setLoteId(loteId);
        nota.setContenido(contenido.trim());
        nota.setFechaHora(LocalDateTime.now());
        return notaRepository.save(nota);
    }

    public List<Nota> listarPorLote(Long loteId) {
        return notaRepository.findByLoteId(loteId);
    }
}
