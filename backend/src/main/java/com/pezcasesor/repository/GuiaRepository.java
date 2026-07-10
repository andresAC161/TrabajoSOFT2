package com.pezcasesor.repository;

import com.pezcasesor.model.Guia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuiaRepository extends JpaRepository<Guia, Long> {
    List<Guia> findByEspecieIgnoreCase(String especie);
    List<Guia> findByEspecieIgnoreCaseAndCategoria(String especie, String categoria);
    List<Guia> findByEspecieIgnoreCaseAndCategoriaAndParametroIn(String especie, String categoria, List<String> parametros);
}
