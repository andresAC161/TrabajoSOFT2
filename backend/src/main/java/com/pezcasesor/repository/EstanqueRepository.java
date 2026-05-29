package com.pezcasesor.repository;

import com.pezcasesor.model.Estanque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstanqueRepository extends JpaRepository<Estanque, Long> {
    List<Estanque> findByUsuarioId(Long usuarioId);
}
