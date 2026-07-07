package com.pezcasesor.repository;

import com.pezcasesor.model.ParametroAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParametroAguaRepository extends JpaRepository<ParametroAgua, Long> {
    List<ParametroAgua> findByEstanqueIdOrderByFechaRegistroDesc(Long estanqueId);
    Optional<ParametroAgua> findFirstByEstanqueIdOrderByFechaRegistroDesc(Long estanqueId);
}
