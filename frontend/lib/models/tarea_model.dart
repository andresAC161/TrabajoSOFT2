class Tarea {
  final int id;
  final int usuarioId;
  final int? estanqueId;
  final int? loteId;
  final String nombre;
  final String? descripcion;
  final String fechaHora;
  final String estado;
  final bool notificado;

  Tarea({
    required this.id,
    required this.usuarioId,
    this.estanqueId,
    this.loteId,
    required this.nombre,
    this.descripcion,
    required this.fechaHora,
    required this.estado,
    required this.notificado,
  });

  factory Tarea.fromJson(Map<String, dynamic> json) => Tarea(
        id: json['id'],
        usuarioId: json['usuarioId'],
        estanqueId: json['estanqueId'],
        loteId: json['loteId'],
        nombre: json['nombre'],
        descripcion: json['descripcion'],
        fechaHora: json['fechaHora'],
        estado: json['estado'],
        notificado: json['notificado'] ?? false,
      );
}
