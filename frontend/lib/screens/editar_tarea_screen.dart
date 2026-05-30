import 'package:flutter/material.dart';
import '../models/tarea_model.dart';
import '../services/tarea_service.dart';

class EditarTareaScreen extends StatefulWidget {
  final Tarea tarea;
  const EditarTareaScreen({super.key, required this.tarea});

  @override
  State<EditarTareaScreen> createState() => _EditarTareaScreenState();
}

class _EditarTareaScreenState extends State<EditarTareaScreen> {
  final _formKey = GlobalKey<FormState>();
  String? _nuevoEstado;
  late final TextEditingController _nombreCtrl;
  late final TextEditingController _descripcionCtrl;
  late final TextEditingController _fechaCtrl;
  bool _cargando = false;

  final _service = TareaService();

  @override
  void initState() {
    super.initState();
    _nombreCtrl = TextEditingController(text: widget.tarea.nombre);
    _descripcionCtrl = TextEditingController(text: widget.tarea.descripcion ?? '');
    _fechaCtrl = TextEditingController(
      text: widget.tarea.fechaHora.substring(0, 16),
    );
  }

  Future<void> _seleccionarFechaHora() async {
    final fecha = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 365)),
    );
    if (fecha == null || !mounted) return;
    final hora = await showTimePicker(context: context, initialTime: TimeOfDay.now());
    if (hora == null) return;
    final dt = DateTime(fecha.year, fecha.month, fecha.day, hora.hour, hora.minute);
    _fechaCtrl.text = dt.toIso8601String().substring(0, 16);
  }

  Future<void> _guardar() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _cargando = true);
    try {
      await _service.actualizar(widget.tarea.id, {
        'nombre': _nombreCtrl.text.trim(),
        if (_descripcionCtrl.text.trim().isNotEmpty)
          'descripcion': _descripcionCtrl.text.trim(),
        if (_nuevoEstado != null) 'estado': _nuevoEstado,
        'fechaHora': _fechaCtrl.text.trim(),
      });
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Tarea actualizada correctamente')),
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
      appBar: AppBar(title: const Text('Editar tarea')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nombreCtrl,
                decoration: const InputDecoration(labelText: 'Nombre *'),
                validator: (v) => v == null || v.trim().isEmpty ? 'Ingrese el nombre' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _descripcionCtrl,
                decoration: const InputDecoration(labelText: 'Descripción'),
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
                validator: (v) => v == null || v.trim().isEmpty ? 'Seleccione fecha y hora' : null,
              ),
              if (widget.tarea.estado == 'pendiente') ...[
                const SizedBox(height: 12),
                DropdownButtonFormField<String>(
                  value: _nuevoEstado,
                  decoration: const InputDecoration(labelText: 'Cambiar estado (opcional)'),
                  hint: const Text('Sin cambio'),
                  items: const [
                    DropdownMenuItem(value: 'completada', child: Text('Completada')),
                    DropdownMenuItem(value: 'cancelada', child: Text('Cancelada')),
                  ],
                  onChanged: (v) => setState(() => _nuevoEstado = v),
                ),
              ] else
                Padding(
                  padding: const EdgeInsets.only(top: 12),
                  child: Text('Estado actual: ${widget.tarea.estado}',
                      style: const TextStyle(color: Colors.grey)),
                ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: _cargando ? null : _guardar,
                child: _cargando
                    ? const CircularProgressIndicator()
                    : const Text('Guardar cambios'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
