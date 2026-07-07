class Nota {
  final int id;
  final int loteId;
  final String contenido;
  final String fechaHora;

  Nota({
    required this.id,
    required this.loteId,
    required this.contenido,
    required this.fechaHora,
  });

  factory Nota.fromJson(Map<String, dynamic> json) => Nota(
        id: json['id'],
        loteId: json['loteId'],
        contenido: json['contenido'],
        fechaHora: json['fechaHora'].toString(),
      );
}
