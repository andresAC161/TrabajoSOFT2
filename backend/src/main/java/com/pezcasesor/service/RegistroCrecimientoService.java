package com.pezcasesor.service;

import com.pezcasesor.model.RegistroCrecimiento;
import com.pezcasesor.repository.RegistroCrecimientoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroCrecimientoService {

    private final RegistroCrecimientoRepository registroRepository;

    public RegistroCrecimientoService(RegistroCrecimientoRepository registroRepository) {
        this.registroRepository = registroRepository;
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
        if (registro.getFechaMuestreo() == null) {
            registro.setFechaMuestreo(LocalDate.now());
        }
        return registroRepository.save(registro);
    }

    public List<RegistroCrecimiento> listarPorLote(Long loteId) {
        return registroRepository.findByLoteIdOrderByFechaMuestreoAsc(loteId);
    }
}
