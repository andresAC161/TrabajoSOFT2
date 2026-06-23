import 'package:flutter/material.dart';

class TarjetaCrecimiento extends StatelessWidget {
  final String fecha;
  final double pesoPromedioG;

  const TarjetaCrecimiento({
    super.key,
    required this.fecha,
    required this.pesoPromedioG,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
      child: ListTile(
        leading: const Icon(Icons.scale, color: Colors.teal),
        title: Text('Peso promedio: ${pesoPromedioG.toStringAsFixed(1)} g'),
        subtitle: Text('Fecha: $fecha'),
      ),
    );
  }
}
