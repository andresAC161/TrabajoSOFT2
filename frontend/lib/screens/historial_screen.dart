import 'package:flutter/material.dart';
import '../models/bitacora_model.dart';
import '../services/bitacora_service.dart';

class HistorialScreen extends StatefulWidget {
  const HistorialScreen({super.key});

  @override
  State<HistorialScreen> createState() => _HistorialScreenState();
}

class _HistorialScreenState extends State<HistorialScreen> {
  final _service = BitacoraService();
  DateTimeRange? _rangoFechas;
  List<Bitacora> _registros = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _cargar();
  }

  Future<void> _cargar() async {
    setState(() => _cargando = true);
    try {
      final registros = await _service.listar();
      setState(() => _registros = registros);
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error al cargar el historial: $e')),
        );
      }
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

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

  List<Bitacora> get _registrosFiltrados {
    if (_rangoFechas == null) return _registros;
    return _registros.where((r) {
      final fecha = DateTime.tryParse(r.fechaRegistro);
      if (fecha == null) return true;
      final inicio = _rangoFechas!.start;
      final fin = _rangoFechas!.end.add(const Duration(days: 1));
      return fecha.isAfter(inicio) && fecha.isBefore(fin);
    }).toList();
  }

  String _formatearFecha(String iso) {
    final fecha = DateTime.tryParse(iso);
    if (fecha == null) return iso;
    final dosDigitos = (int n) => n.toString().padLeft(2, '0');
    return '${dosDigitos(fecha.day)}/${dosDigitos(fecha.month)}/${fecha.year} '
        '${dosDigitos(fecha.hour)}:${dosDigitos(fecha.minute)}';
  }

  String _formatearRango(DateTime d) => '${d.day}/${d.month}/${d.year}';

  @override
  Widget build(BuildContext context) {
    final registros = _registrosFiltrados;
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
                        : '${_formatearRango(_rangoFechas!.start)} - ${_formatearRango(_rangoFechas!.end)}',
                  ),
                ),
                if (_rangoFechas != null)
                  IconButton(
                    icon: const Icon(Icons.clear),
                    tooltip: 'Quitar filtro',
                    onPressed: () => setState(() => _rangoFechas = null),
                  ),
                TextButton.icon(
                  onPressed: _seleccionarRango,
                  icon: const Icon(Icons.date_range),
                  label: const Text('Filtrar por fecha'),
                ),
              ],
            ),
          ),
          const Divider(height: 1),
          Expanded(
            child: _cargando
                ? const Center(child: CircularProgressIndicator())
                : registros.isEmpty
                    ? const Center(
                        child: Text(
                          'Aún no hay actividades registradas.',
                          style: TextStyle(color: Colors.grey),
                        ),
                      )
                    : RefreshIndicator(
                        onRefresh: _cargar,
                        child: ListView.builder(
                          padding: const EdgeInsets.all(8),
                          itemCount: registros.length,
                          itemBuilder: (context, index) {
                            final r = registros[index];
                            return Card(
                              child: ListTile(
                                leading: const Icon(Icons.history, color: Colors.teal),
                                title: Text(r.accion),
                                subtitle: Text(
                                  '${r.usuarioNombre} · ${_formatearFecha(r.fechaRegistro)}',
                                ),
                              ),
                            );
                          },
                        ),
                      ),
          ),
        ],
      ),
    );
  }
}
