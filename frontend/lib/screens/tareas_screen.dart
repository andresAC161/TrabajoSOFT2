import 'package:flutter/material.dart';
import '../models/tarea_model.dart';
import '../services/tarea_service.dart';

class TareasScreen extends StatefulWidget {
  final int usuarioId;

  const TareasScreen({super.key, required this.usuarioId});

  @override
  State<TareasScreen> createState() => _TareasScreenState();
}

class _TareasScreenState extends State<TareasScreen> {
  final TareaService _tareaService = TareaService();
  List<Tarea> _tareas = [];
  String _filtro = 'todas';
  bool _cargando = true;

  final List<Map<String, String>> _filtros = [
    {'valor': 'todas', 'etiqueta': 'Todas'},
    {'valor': 'pendiente', 'etiqueta': 'Pendiente'},
    {'valor': 'completada', 'etiqueta': 'Completada'},
    {'valor': 'cancelada', 'etiqueta': 'Cancelada'},
  ];

  @override
  void initState() {
    super.initState();
    _cargarTareas();
  }

  Future<void> _cargarTareas() async {
    setState(() => _cargando = true);
    try {
      final tareas = await _tareaService.listar(widget.usuarioId);
      setState(() => _tareas = tareas);
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error al cargar tareas: $e')),
        );
      }
    } finally {
      setState(() => _cargando = false);
    }
  }

  List<Tarea> get _tareasFiltradas {
    if (_filtro == 'todas') return _tareas;
    return _tareas.where((t) => t.estado == _filtro).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Mis tareas')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: _filtros.map((f) {
                  final seleccionado = _filtro == f['valor'];
                  return Padding(
                    padding: const EdgeInsets.only(right: 8),
                    child: FilterChip(
                      label: Text(f['etiqueta']!),
                      selected: seleccionado,
                      onSelected: (_) => setState(() => _filtro = f['valor']!),
                    ),
                  );
                }).toList(),
              ),
            ),
          ),
          const Divider(height: 1),
          Expanded(
            child: _cargando
                ? const Center(child: CircularProgressIndicator())
                : _tareasFiltradas.isEmpty
                    ? Center(
                        child: Text(
                          'No hay tareas $_filtro',
                          style: const TextStyle(color: Colors.grey),
                        ),
                      )
                    : ListView.builder(
                        itemCount: _tareasFiltradas.length,
                        itemBuilder: (context, index) {
                          final tarea = _tareasFiltradas[index];
                          return ListTile(
                            leading: Icon(
                              tarea.estado == 'completada'
                                  ? Icons.check_circle
                                  : tarea.estado == 'cancelada'
                                      ? Icons.cancel
                                      : Icons.radio_button_unchecked,
                              color: tarea.estado == 'completada'
                                  ? Colors.green
                                  : tarea.estado == 'cancelada'
                                      ? Colors.red
                                      : Colors.orange,
                            ),
                            title: Text(tarea.nombre),
                            subtitle: Text(tarea.fechaHora),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}
