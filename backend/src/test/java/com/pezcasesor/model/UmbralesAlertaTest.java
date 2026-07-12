package com.pezcasesor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas de caja negra (particion de equivalencia y valores limite)
// sobre las reglas de umbral por especie.
class UmbralesAlertaTest {

    // ---------- pH ----------

    @Test
    void ph_trucha_dentroDeRango_noGeneraAlerta() {
        assertFalse(UmbralesAlerta.phFueraDeRango("trucha", 6.5)); // limite inferior valido
        assertFalse(UmbralesAlerta.phFueraDeRango("trucha", 7.0)); // valor central
        assertFalse(UmbralesAlerta.phFueraDeRango("trucha", 8.0)); // limite superior valido
    }

    @Test
    void ph_trucha_fueraDeRango_generaAlerta() {
        assertTrue(UmbralesAlerta.phFueraDeRango("trucha", 6.4)); // por debajo del minimo
        assertTrue(UmbralesAlerta.phFueraDeRango("trucha", 8.1)); // por encima del maximo
    }

    @Test
    void ph_tilapia_respetaSusPropiosLimites() {
        assertFalse(UmbralesAlerta.phFueraDeRango("tilapia", 6.0)); // limite inferior valido
        assertFalse(UmbralesAlerta.phFueraDeRango("tilapia", 9.0)); // limite superior valido
        assertTrue(UmbralesAlerta.phFueraDeRango("tilapia", 5.9));  // por debajo del minimo
        assertTrue(UmbralesAlerta.phFueraDeRango("tilapia", 9.1));  // por encima del maximo
    }

    // ---------- Temperatura ----------

    @Test
    void temperatura_trucha_dentroDeRango_noGeneraAlerta() {
        assertFalse(UmbralesAlerta.temperaturaFueraDeRango("trucha", 10.0)); // limite inferior
        assertFalse(UmbralesAlerta.temperaturaFueraDeRango("trucha", 18.0)); // limite superior
    }

    @Test
    void temperatura_trucha_fueraDeRango_generaAlerta() {
        assertTrue(UmbralesAlerta.temperaturaFueraDeRango("trucha", 9.9));  // por debajo del minimo
        assertTrue(UmbralesAlerta.temperaturaFueraDeRango("trucha", 18.1)); // por encima del maximo
    }

    @Test
    void temperatura_tilapia_respetaSusPropiosLimites() {
        assertFalse(UmbralesAlerta.temperaturaFueraDeRango("tilapia", 32.0)); // limite superior valido
        assertTrue(UmbralesAlerta.temperaturaFueraDeRango("tilapia", 32.1));  // por encima del maximo
    }

    // ---------- Oxigeno ----------

    @Test
    void oxigeno_bajoElMinimo_generaAlerta() {
        assertFalse(UmbralesAlerta.oxigenoFueraDeRango("trucha", 6.0));  // limite valido
        assertTrue(UmbralesAlerta.oxigenoFueraDeRango("trucha", 5.9));   // por debajo del minimo
        assertFalse(UmbralesAlerta.oxigenoFueraDeRango("tilapia", 4.0)); // limite valido
        assertTrue(UmbralesAlerta.oxigenoFueraDeRango("tilapia", 3.9));  // por debajo del minimo
    }

    // ---------- Amoniaco ----------

    @Test
    void amoniaco_sobreElMaximo_generaAlerta() {
        assertFalse(UmbralesAlerta.amoniacoFueraDeRango("trucha", 0.02));  // limite valido
        assertTrue(UmbralesAlerta.amoniacoFueraDeRango("trucha", 0.03));   // por encima del maximo
        assertFalse(UmbralesAlerta.amoniacoFueraDeRango("tilapia", 0.5));  // limite valido
        assertTrue(UmbralesAlerta.amoniacoFueraDeRango("tilapia", 0.51));  // por encima del maximo
    }

    // ---------- Casos especiales ----------

    @Test
    void especieDesconocida_nuncaGeneraAlerta() {
        assertFalse(UmbralesAlerta.phFueraDeRango("carpa", 2.0));
        assertFalse(UmbralesAlerta.temperaturaFueraDeRango("carpa", 50.0));
        assertFalse(UmbralesAlerta.oxigenoFueraDeRango("carpa", 0.0));
        assertFalse(UmbralesAlerta.amoniacoFueraDeRango("carpa", 99.0));
        assertNull(UmbralesAlerta.rangoTemperatura("carpa"));
        assertNull(UmbralesAlerta.rangoPh("carpa"));
    }

    @Test
    void rangos_devuelvenLimitesEsperados() {
        assertArrayEquals(new double[]{10.0, 18.0}, UmbralesAlerta.rangoTemperatura("trucha"));
        assertArrayEquals(new double[]{6.5, 8.0}, UmbralesAlerta.rangoPh("trucha"));
        assertArrayEquals(new double[]{25.0, 32.0}, UmbralesAlerta.rangoTemperatura("tilapia"));
        assertArrayEquals(new double[]{6.0, 9.0}, UmbralesAlerta.rangoPh("tilapia"));
    }
}
