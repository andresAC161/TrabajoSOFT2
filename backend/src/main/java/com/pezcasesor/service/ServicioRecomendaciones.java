package com.pezcasesor.service;

import com.pezcasesor.model.Lote;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.model.ParametroAgua;
import com.pezcasesor.model.UmbralesAlerta;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.ParametroAguaRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ServicioRecomendaciones {

    private static final int LECTURAS_PARA_TENDENCIA = 3;

    private final ParametroAguaRepository parametroAguaRepository;
    private final LoteRepository loteRepository;

    public ServicioRecomendaciones(ParametroAguaRepository parametroAguaRepository,
                                    LoteRepository loteRepository) {
        this.parametroAguaRepository = parametroAguaRepository;
        this.loteRepository = loteRepository;
    }

    public List<String> evaluarTendencias(Long estanqueId) {
        List<ParametroAgua> historicos =
            parametroAguaRepository.findByEstanqueIdOrderByFechaRegistroDesc(estanqueId);
        List<String> consejos = new ArrayList<>();
        if (historicos.size() < LECTURAS_PARA_TENDENCIA) {
            return consejos;
        }
        List<ParametroAgua> ultimas = historicos.subList(0, LECTURAS_PARA_TENDENCIA);

        if (tendenciaSostenidaALaBaja(ultimas, p -> p.getOxigenoMgl().doubleValue())) {
            consejos.add("Aumentar aireación nocturna");
        }
        if (tendenciaSostenidaAlAlza(ultimas, p -> p.getAmoniacoMgl().doubleValue())) {
            consejos.add("Realizar recambio de agua para reducir el amoníaco acumulado");
        }

        obtenerEspecieActiva(estanqueId).ifPresent(especie -> {
            evaluarTendenciaHaciaLimite(ultimas, UmbralesAlerta.rangoTemperatura(especie),
                p -> p.getTemperaturaC().doubleValue(),
                "Proteger el estanque del frío (la temperatura viene bajando de forma sostenida)",
                "Aumentar sombra o profundidad (la temperatura viene subiendo de forma sostenida)",
                consejos);
            evaluarTendenciaHaciaLimite(ultimas, UmbralesAlerta.rangoPh(especie),
                p -> p.getPh().doubleValue(),
                "Corregir la acidez del agua (el pH viene bajando de forma sostenida)",
                "Corregir la alcalinidad del agua (el pH viene subiendo de forma sostenida)",
                consejos);
        });

        return consejos;
    }

    private Optional<String> obtenerEspecieActiva(Long estanqueId) {
        return loteRepository.findByEstanqueId(estanqueId).stream()
            .filter(l -> l.getEstado() == LoteEstado.activo)
            .findFirst()
            .map(Lote::getEspecie);
    }

    private void evaluarTendenciaHaciaLimite(List<ParametroAgua> ultimasDesc, double[] rango,
                                              Function<ParametroAgua, Double> valor,
                                              String mensajeSiBaja, String mensajeSiSube,
                                              List<String> consejos) {
        if (rango == null) return;
        double centro = (rango[0] + rango[1]) / 2;
        double actual = valor.apply(ultimasDesc.get(0));
        if (actual < centro && tendenciaSostenidaALaBaja(ultimasDesc, valor)) {
            consejos.add(mensajeSiBaja);
        } else if (actual > centro && tendenciaSostenidaAlAlza(ultimasDesc, valor)) {
            consejos.add(mensajeSiSube);
        }
    }

    private boolean tendenciaSostenidaALaBaja(List<ParametroAgua> ultimasDesc, Function<ParametroAgua, Double> valor) {
        for (int i = 0; i < ultimasDesc.size() - 1; i++) {
            if (valor.apply(ultimasDesc.get(i)) >= valor.apply(ultimasDesc.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private boolean tendenciaSostenidaAlAlza(List<ParametroAgua> ultimasDesc, Function<ParametroAgua, Double> valor) {
        for (int i = 0; i < ultimasDesc.size() - 1; i++) {
            if (valor.apply(ultimasDesc.get(i)) <= valor.apply(ultimasDesc.get(i + 1))) {
                return false;
            }
        }
        return true;
    }
}
