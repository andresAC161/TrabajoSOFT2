class Notificacion {
  final int id;
  final int usuarioId;
  final int tareaId;
  final String mensaje;
  final String fechaEnvio;
  final bool leida;

  Notificacion({
    required this.id,
    required this.usuarioId,
    required this.tareaId,
    required this.mensaje,
    required this.fechaEnvio,
    required this.leida,
  });

  factory Notificacion.fromJson(Map<String, dynamic> json) => Notificacion(
        id: json['id'],
        usuarioId: json['usuarioId'],
        tareaId: json['tareaId'],
        mensaje: json['mensaje'],
        fechaEnvio: json['fechaEnvio'],
        leida: json['leida'] ?? false,
      );
}
