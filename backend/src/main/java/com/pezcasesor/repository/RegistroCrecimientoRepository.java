package com.pezcasesor.repository;

import com.pezcasesor.model.RegistroCrecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistroCrecimientoRepository extends JpaRepository<RegistroCrecimiento, Long> {
    List<RegistroCrecimiento> findByLoteIdOrderByFechaMuestreoAsc(Long loteId);
}
