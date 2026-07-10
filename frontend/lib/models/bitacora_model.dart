class Bitacora {
  final int id;
  final int usuarioId;
  final String usuarioNombre;
  final String accion;
  final String fechaRegistro;

  Bitacora({
    required this.id,
    required this.usuarioId,
    required this.usuarioNombre,
    required this.accion,
    required this.fechaRegistro,
  });

  factory Bitacora.fromJson(Map<String, dynamic> json) => Bitacora(
        id: json['id'],
        usuarioId: json['usuarioId'],
        usuarioNombre: json['usuarioNombre'],
        accion: json['accion'],
        fechaRegistro: json['fechaRegistro'],
      );
}
