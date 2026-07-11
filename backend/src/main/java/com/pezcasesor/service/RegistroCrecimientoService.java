package com.pezcasesor.service;

import com.pezcasesor.model.Lote;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.model.RegistroCrecimiento;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.RegistroCrecimientoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroCrecimientoService {

    private final RegistroCrecimientoRepository registroRepository;
    private final LoteRepository loteRepository;

    public RegistroCrecimientoService(RegistroCrecimientoRepository registroRepository,
                                       LoteRepository loteRepository) {
        this.registroRepository = registroRepository;
        this.loteRepository = loteRepository;
    }

    public RegistroCrecimiento registrar(RegistroCrecimiento registro) {
        if (registro.getPesoPromedioG() == null || registro.getPesoPromedioG().signum() <= 0) {
            throw new IllegalArgumentException("El peso promedio debe ser mayor a 0.");
        }
        if (registro.getTallaCm() != null && registro.getTallaCm().signum() <= 0) {
            throw new IllegalArgumentException("La talla debe ser mayor a 0.");
        }
        if (registro.getMortalidad() != null && registro.getMortalidad() < 0) {
            throw new IllegalArgumentException("La mortalidad no puede ser negativa.");
        }
        Lote lote = loteRepository.findById(registro.getLoteId())
            .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado."));
        if (lote.getEstado() == LoteEstado.finalizado) {
            throw new IllegalStateException("El lote ya está finalizado, no se pueden agregar registros de crecimiento.");
        }
        if (registro.getFechaMuestreo() == null) {
            registro.setFechaMuestreo(LocalDate.now());
        }
        return registroRepository.save(registro);
    }

    public List<RegistroCrecimiento> listarPorLote(Long loteId) {
        return registroRepository.findByLoteIdOrderByFechaMuestreoAsc(loteId);
    }
}
