import 'package:flutter/material.dart';
import '../models/estanque_model.dart';
import '../services/estanque_service.dart';
import '../services/tarea_service.dart';

class ProgramarTareaScreen extends StatefulWidget {
  final int usuarioId;
  const ProgramarTareaScreen({super.key, required this.usuarioId});

  @override
  State<ProgramarTareaScreen> createState() => _ProgramarTareaScreenState();
}

class _ProgramarTareaScreenState extends State<ProgramarTareaScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nombreCtrl = TextEditingController();
  final _descripcionCtrl = TextEditingController();
  final _fechaCtrl = TextEditingController();
  bool _cargando = false;
  int? _estanqueIdSeleccionado;
  List<Estanque> _estanques = [];

  final _tareaService = TareaService();
  final _estanqueService = EstanqueService();

  @override
  void initState() {
    super.initState();
    _cargarEstanques();
  }

  Future<void> _cargarEstanques() async {
    try {
      final lista = await _estanqueService.listar(widget.usuarioId);
      setState(() => _estanques = lista);
    } catch (_) {}
  }

  Future<void> _seleccionarFechaHora() async {
    final fecha = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 365)),
    );
    if (fecha == null || !mounted) return;
    final hora =
        await showTimePicker(context: context, initialTime: TimeOfDay.now());
    if (hora == null) return;
    final dt =
        DateTime(fecha.year, fecha.month, fecha.day, hora.hour, hora.minute);
    _fechaCtrl.text = dt.toIso8601String().substring(0, 16);
  }

  Future<void> _crear() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _cargando = true);
    try {
      await _tareaService.crear(
        usuarioId: widget.usuarioId,
        estanqueId: _estanqueIdSeleccionado,
        nombre: _nombreCtrl.text.trim(),
        descripcion: _descripcionCtrl.text.trim().isEmpty
            ? null
            : _descripcionCtrl.text.trim(),
        fechaHora: _fechaCtrl.text.trim(),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Tarea programada correctamente')),
      );
      Navigator.pop(context, true);
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(e.toString().replaceAll('Exception: ', ''))),
      );
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Programar tarea')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nombreCtrl,
                decoration:
                    const InputDecoration(labelText: 'Nombre de la tarea *'),
                validator: (v) =>
                    v == null || v.trim().isEmpty ? 'Ingrese el nombre' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _descripcionCtrl,
                decoration:
                    const InputDecoration(labelText: 'Descripción (opcional)'),
                maxLines: 2,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _fechaCtrl,
                decoration: const InputDecoration(
                  labelText: 'Fecha y hora *',
                  suffixIcon: Icon(Icons.schedule),
                ),
                readOnly: true,
                onTap: _seleccionarFechaHora,
                validator: (v) => v == null || v.trim().isEmpty
                    ? 'Seleccione fecha y hora'
                    : null,
              ),
              const SizedBox(height: 12),
              if (_estanques.isNotEmpty)
                DropdownButtonFormField<int?>(
                  initialValue: _estanqueIdSeleccionado,
                  decoration: const InputDecoration(
                      labelText: 'Estanque asociado (opcional)'),
                  items: [
                    const DropdownMenuItem(
                        value: null, child: Text('Sin estanque')),
                    ..._estanques.map((e) => DropdownMenuItem(
                          value: e.id,
                          child: Text(e.nombre),
                        )),
                  ],
                  onChanged: (v) => setState(() => _estanqueIdSeleccionado = v),
                ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: _cargando ? null : _crear,
                child: _cargando
                    ? const CircularProgressIndicator()
                    : const Text('Programar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
