package com.pezcasesor.service;

import com.pezcasesor.model.Tarea;

public interface TareaVencimientoObserver {
    void onTareaProxima(Tarea tarea);
}
