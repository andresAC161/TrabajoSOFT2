class ParametroAgua {
  final int id;
  final int estanqueId;
  final int usuarioId;
  final String ph;
  final String temperaturaC;
  final String oxigenoMgl;
  final String amoniacoMgl;
  final String fechaRegistro;

  ParametroAgua({
    required this.id,
    required this.estanqueId,
    required this.usuarioId,
    required this.ph,
    required this.temperaturaC,
    required this.oxigenoMgl,
    required this.amoniacoMgl,
    required this.fechaRegistro,
  });

  factory ParametroAgua.fromJson(Map<String, dynamic> json) => ParametroAgua(
        id: json['id'],
        estanqueId: json['estanqueId'],
        usuarioId: json['usuarioId'],
        ph: json['ph'].toString(),
        temperaturaC: json['temperaturaC'].toString(),
        oxigenoMgl: json['oxigenoMgl'].toString(),
        amoniacoMgl: json['amoniacoMgl'].toString(),
        fechaRegistro: json['fechaRegistro'],
      );
}
