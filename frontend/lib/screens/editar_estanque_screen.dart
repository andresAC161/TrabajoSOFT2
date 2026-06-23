import 'package:flutter/material.dart';
import '../models/estanque_model.dart';
import '../services/estanque_service.dart';

class EditarEstanqueScreen extends StatefulWidget {
  final Estanque estanque;
  const EditarEstanqueScreen({super.key, required this.estanque});

  @override
  State<EditarEstanqueScreen> createState() => _EditarEstanqueScreenState();
}

class _EditarEstanqueScreenState extends State<EditarEstanqueScreen> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _nombreCtrl;
  late final TextEditingController _capacidadCtrl;
  late final TextEditingController _localizacionCtrl;
  late String _tipoAgua;
  bool _cargando = false;

  final _service = EstanqueService();

  @override
  void initState() {
    super.initState();
    _nombreCtrl = TextEditingController(text: widget.estanque.nombre);
    _capacidadCtrl =
        TextEditingController(text: widget.estanque.capacidadLitros);
    _localizacionCtrl =
        TextEditingController(text: widget.estanque.localizacion ?? '');
    _tipoAgua = widget.estanque.tipoAgua;
  }

  Future<void> _guardar() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _cargando = true);
    try {
      await _service.editar(widget.estanque.id, {
        'nombre': _nombreCtrl.text.trim(),
        'tipoAgua': _tipoAgua,
        'capacidadLitros': _capacidadCtrl.text.trim(),
        if (_localizacionCtrl.text.trim().isNotEmpty)
          'localizacion': _localizacionCtrl.text.trim(),
      });
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Estanque actualizado correctamente')),
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
      appBar: AppBar(title: const Text('Editar estanque')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nombreCtrl,
                decoration: const InputDecoration(labelText: 'Nombre *'),
                validator: (v) =>
                    v == null || v.trim().isEmpty ? 'Ingrese el nombre' : null,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: _tipoAgua,
                decoration: const InputDecoration(labelText: 'Tipo de agua'),
                items: const [
                  DropdownMenuItem(value: 'dulce', child: Text('Dulce')),
                  DropdownMenuItem(value: 'salada', child: Text('Salada')),
                  DropdownMenuItem(value: 'salobre', child: Text('Salobre')),
                ],
                onChanged: (v) => setState(() => _tipoAgua = v!),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _capacidadCtrl,
                decoration:
                    const InputDecoration(labelText: 'Capacidad (litros) *'),
                keyboardType: TextInputType.number,
                validator: (v) {
                  if (v == null || v.trim().isEmpty)
                    return 'Ingrese la capacidad';
                  final num = double.tryParse(v.trim());
                  if (num == null) return 'Ingrese un número válido';
                  if (num <= 0) return 'La capacidad debe ser mayor a 0';
                  return null;
                },
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _localizacionCtrl,
                decoration: const InputDecoration(labelText: 'Localización'),
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
