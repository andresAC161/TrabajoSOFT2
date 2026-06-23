package com.pezcasesor.model;

public class UmbralesAlerta {

    public static final double PH_MIN_TRUCHA = 6.5;
    public static final double PH_MAX_TRUCHA = 8.0;
    public static final double TEMPERATURA_MIN_TRUCHA = 10.0;
    public static final double TEMPERATURA_MAX_TRUCHA = 18.0;
    public static final double OXIGENO_MIN_TRUCHA = 6.0;

    public static final double PH_MIN_TILAPIA = 6.0;
    public static final double PH_MAX_TILAPIA = 9.0;
    public static final double TEMPERATURA_MIN_TILAPIA = 25.0;
    public static final double TEMPERATURA_MAX_TILAPIA = 32.0;
    public static final double OXIGENO_MIN_TILAPIA = 4.0;

    public static boolean phFueraDeRango(String especie, double ph) {
        return switch (especie.toLowerCase()) {
            case "trucha"  -> ph < PH_MIN_TRUCHA  || ph > PH_MAX_TRUCHA;
            case "tilapia" -> ph < PH_MIN_TILAPIA || ph > PH_MAX_TILAPIA;
            default        -> false;
        };
    }

    public static boolean temperaturaFueraDeRango(String especie, double temperatura) {
        return switch (especie.toLowerCase()) {
            case "trucha"  -> temperatura < TEMPERATURA_MIN_TRUCHA  || temperatura > TEMPERATURA_MAX_TRUCHA;
            case "tilapia" -> temperatura < TEMPERATURA_MIN_TILAPIA || temperatura > TEMPERATURA_MAX_TILAPIA;
            default        -> false;
        };
    }

    public static boolean oxigenoFueraDeRango(String especie, double oxigeno) {
        return switch (especie.toLowerCase()) {
            case "trucha"  -> oxigeno < OXIGENO_MIN_TRUCHA;
            case "tilapia" -> oxigeno < OXIGENO_MIN_TILAPIA;
            default        -> false;
        };
    }
}
