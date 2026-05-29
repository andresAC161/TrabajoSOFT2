class Usuario {
  final int id;
  final String nombre;
  final String correo;
  final String rol;
  final String? ubicacion;
  final String fechaRegistro;

  Usuario({
    required this.id,
    required this.nombre,
    required this.correo,
    required this.rol,
    this.ubicacion,
    required this.fechaRegistro,
  });

  factory Usuario.fromJson(Map<String, dynamic> json) => Usuario(
        id: json['id'],
        nombre: json['nombre'],
        correo: json['correo'],
        rol: json['rol'],
        ubicacion: json['ubicacion'],
        fechaRegistro: json['fechaRegistro'],
      );
}
