class RegistroCrecimiento {
  final int id;
  final int loteId;
  final double pesoPromedioG;
  final double? tallaCm;
  final int? mortalidad;
  final String fechaMuestreo;

  RegistroCrecimiento({
    required this.id,
    required this.loteId,
    required this.pesoPromedioG,
    this.tallaCm,
    this.mortalidad,
    required this.fechaMuestreo,
  });

  factory RegistroCrecimiento.fromJson(Map<String, dynamic> json) => RegistroCrecimiento(
        id: json['id'],
        loteId: json['loteId'],
        pesoPromedioG: double.parse(json['pesoPromedioG'].toString()),
        tallaCm: json['tallaCm'] == null ? null : double.parse(json['tallaCm'].toString()),
        mortalidad: json['mortalidad'],
        fechaMuestreo: json['fechaMuestreo'],
      );
}
