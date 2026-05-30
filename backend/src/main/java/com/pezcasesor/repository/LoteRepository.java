package com.pezcasesor.repository;

import com.pezcasesor.model.Lote;
import com.pezcasesor.model.LoteEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    boolean existsByEstanqueIdAndEstado(Long estanqueId, LoteEstado estado);
    List<Lote> findByEstanqueId(Long estanqueId);
}
