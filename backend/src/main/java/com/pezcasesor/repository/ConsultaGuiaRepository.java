package com.pezcasesor.repository;

import com.pezcasesor.model.ConsultaGuia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaGuiaRepository extends JpaRepository<ConsultaGuia, Long> {
}
