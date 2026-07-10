class Guia {
  final int id;
  final String especie;
  final String categoria;
  final String? parametro;
  final String titulo;
  final String contenido;

  Guia({
    required this.id,
    required this.especie,
    required this.categoria,
    this.parametro,
    required this.titulo,
    required this.contenido,
  });

  factory Guia.fromJson(Map<String, dynamic> json) => Guia(
        id: json['id'],
        especie: json['especie'],
        categoria: json['categoria'],
        parametro: json['parametro'],
        titulo: json['titulo'],
        contenido: json['contenido'],
      );
}
