class Lote {
  final int id;
  final int estanqueId;
  final String especie;
  final int cantidad;
  final String fechaSiembra;
  final String pesoInicialG;
  final String estado;
  final String? fechaFin;

  Lote({
    required this.id,
    required this.estanqueId,
    required this.especie,
    required this.cantidad,
    required this.fechaSiembra,
    required this.pesoInicialG,
    required this.estado,
    this.fechaFin,
  });

  factory Lote.fromJson(Map<String, dynamic> json) => Lote(
        id: json['id'],
        estanqueId: json['estanqueId'],
        especie: json['especie'],
        cantidad: json['cantidad'],
        fechaSiembra: json['fechaSiembra'],
        pesoInicialG: json['pesoInicialG'].toString(),
        estado: json['estado'],
        fechaFin: json['fechaFin'],
      );
}
