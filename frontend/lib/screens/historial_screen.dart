import 'package:flutter/material.dart';

class HistorialScreen extends StatefulWidget {
  const HistorialScreen({super.key});

  @override
  State<HistorialScreen> createState() => _HistorialScreenState();
}

class _HistorialScreenState extends State<HistorialScreen> {
  DateTimeRange? _rangoFechas;

  Future<void> _seleccionarRango() async {
    final rango = await showDateRangePicker(
      context: context,
      firstDate: DateTime(2024),
      lastDate: DateTime.now(),
    );
    if (rango != null) {
      setState(() => _rangoFechas = rango);
    }
  }

  String _formatearFecha(DateTime fecha) =>
      '${fecha.day}/${fecha.month}/${fecha.year}';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Historial de actividades')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: Row(
              children: [
                Expanded(
                  child: Text(
                    _rangoFechas == null
                        ? 'Sin filtro de fechas'
                        : '${_formatearFecha(_rangoFechas!.start)} - ${_formatearFecha(_rangoFechas!.end)}',
                  ),
                ),
                TextButton.icon(
                  onPressed: _seleccionarRango,
                  icon: const Icon(Icons.date_range),
                  label: const Text('Filtrar por fecha'),
                ),
              ],
            ),
          ),
          const Divider(),
          const Expanded(
            child: Center(
              child: Text(
                'Conectando con el backend...',
                style: TextStyle(color: Colors.grey),
              ),
            ),
          ),
        ],
      ),
    );
  }
}