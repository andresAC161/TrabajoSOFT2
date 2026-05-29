import 'package:flutter/material.dart';
import '../services/estanque_service.dart';

class RegistrarEstanqueScreen extends StatefulWidget {
  final int usuarioId;
  const RegistrarEstanqueScreen({super.key, required this.usuarioId});

  @override
  State<RegistrarEstanqueScreen> createState() => _RegistrarEstanqueScreenState();
}

class _RegistrarEstanqueScreenState extends State<RegistrarEstanqueScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nombreCtrl = TextEditingController();
  final _capacidadCtrl = TextEditingController();
  final _localizacionCtrl = TextEditingController();
  String _tipoAgua = 'dulce';
  bool _cargando = false;

  final _service = EstanqueService();

  Future<void> _registrar() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _cargando = true);
    try {
      await _service.registrar(
        usuarioId: widget.usuarioId,
        nombre: _nombreCtrl.text.trim(),
        tipoAgua: _tipoAgua,
        capacidadLitros: _capacidadCtrl.text.trim(),
        localizacion: _localizacionCtrl.text.trim().isEmpty
            ? null
            : _localizacionCtrl.text.trim(),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Estanque registrado')),
      );
      Navigator.pop(context, true);
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(e.toString())));
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Registrar estanque')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nombreCtrl,
                decoration: const InputDecoration(labelText: 'Nombre'),
                validator: (v) => v == null || v.isEmpty ? 'Ingrese el nombre' : null,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                value: _tipoAgua,
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
                decoration: const InputDecoration(labelText: 'Capacidad (litros)'),
                keyboardType: TextInputType.number,
                validator: (v) {
                  if (v == null || v.trim().isEmpty) return 'Ingrese la capacidad';
                  final num = double.tryParse(v.trim());
                  if (num == null) return 'Ingrese un número válido';
                  if (num <= 0) return 'La capacidad debe ser mayor a 0';
                  return null;
                },
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _localizacionCtrl,
                decoration: const InputDecoration(labelText: 'Localización (opcional)'),
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: _cargando ? null : _registrar,
                child: _cargando
                    ? const CircularProgressIndicator()
                    : const Text('Guardar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
