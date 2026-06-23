class ConsejosEspecie {
  static const Map<String, List<String>> consejos = {
    'trucha': [
      'Mantener temperatura entre 10°C y 18°C para un crecimiento óptimo.',
      'El oxígeno disuelto debe mantenerse por encima de 6 mg/L.',
      'Alimentar 2 a 3 veces al día con alimento peletizado de 2-3 mm.',
      'Realizar recambios de agua del 20% cada semana.',
      'Monitorear el pH entre 6.5 y 8.0 diariamente.',
    ],
    'tilapia': [
      'La temperatura ideal es entre 25°C y 32°C.',
      'El oxígeno disuelto debe ser mayor a 4 mg/L.',
      'Alimentar 3 veces al día con proteína entre 28% y 32%.',
      'Revisar amoniaco regularmente, no debe superar 0.5 mg/L.',
      'El pH ideal está entre 6.0 y 9.0.',
    ],
  };

  static List<String> obtenerConsejos(String especie) {
    return consejos[especie.toLowerCase()] ?? [];
  }
}
