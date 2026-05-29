class Estanque {
  final int id;
  final int usuarioId;
  final String nombre;
  final String tipoAgua;
  final String capacidadLitros;
  final String? localizacion;
  final String estado;
  final String fechaCreacion;

  Estanque({
    required this.id,
    required this.usuarioId,
    required this.nombre,
    required this.tipoAgua,
    required this.capacidadLitros,
    this.localizacion,
    required this.estado,
    required this.fechaCreacion,
  });

  factory Estanque.fromJson(Map<String, dynamic> json) => Estanque(
        id: json['id'],
        usuarioId: json['usuarioId'],
        nombre: json['nombre'],
        tipoAgua: json['tipoAgua'],
        capacidadLitros: json['capacidadLitros'].toString(),
        localizacion: json['localizacion'],
        estado: json['estado'],
        fechaCreacion: json['fechaCreacion'],
      );
}
