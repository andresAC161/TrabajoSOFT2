package com.pezcasesor.service;

import com.pezcasesor.service.NotificacionAgendaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificacionScheduler {

    private final NotificacionAgendaService agendaService;

    public NotificacionScheduler(NotificacionAgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @Scheduled(fixedDelay = 60000)
    public void verificar() {
        agendaService.procesarTareasProximas();
    }
}
