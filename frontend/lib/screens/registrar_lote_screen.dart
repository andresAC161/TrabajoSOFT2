import 'package:flutter/material.dart';
import '../services/lote_service.dart';

class RegistrarLoteScreen extends StatefulWidget {
  final int estanqueId;
  const RegistrarLoteScreen({super.key, required this.estanqueId});

  @override
  State<RegistrarLoteScreen> createState() => _RegistrarLoteScreenState();
}

class _RegistrarLoteScreenState extends State<RegistrarLoteScreen> {
  final _formKey = GlobalKey<FormState>();
  final _especieCtrl = TextEditingController();
  final _cantidadCtrl = TextEditingController();
  final _fechaCtrl = TextEditingController();
  final _pesoCtrl = TextEditingController();
  bool _cargando = false;

  final _service = LoteService();

  Future<void> _seleccionarFecha() async {
    final fecha = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2020),
      lastDate: DateTime.now(),
    );
    if (fecha != null) {
      _fechaCtrl.text = fecha.toIso8601String().substring(0, 10);
    }
  }

  Future<void> _registrar() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _cargando = true);
    try {
      await _service.registrar(
        estanqueId: widget.estanqueId,
        especie: _especieCtrl.text.trim(),
        cantidad: int.parse(_cantidadCtrl.text.trim()),
        fechaSiembra: _fechaCtrl.text.trim(),
        pesoInicialG: _pesoCtrl.text.trim(),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Lote registrado')),
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
      appBar: AppBar(title: const Text('Registrar lote')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _especieCtrl,
                decoration: const InputDecoration(labelText: 'Especie'),
                validator: (v) => v == null || v.isEmpty ? 'Ingrese la especie' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _cantidadCtrl,
                decoration: const InputDecoration(labelText: 'Cantidad (peces)'),
                keyboardType: TextInputType.number,
                validator: (v) {
                  if (v == null || v.trim().isEmpty) return 'Ingrese la cantidad';
                  final n = int.tryParse(v.trim());
                  if (n == null) return 'Ingrese un número entero válido';
                  if (n <= 0) return 'La cantidad debe ser mayor a 0';
                  return null;
                },
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _fechaCtrl,
                decoration: const InputDecoration(
                  labelText: 'Fecha de siembra',
                  suffixIcon: Icon(Icons.calendar_today),
                ),
                readOnly: true,
                onTap: _seleccionarFecha,
                validator: (v) => v == null || v.isEmpty ? 'Seleccione la fecha' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _pesoCtrl,
                decoration: const InputDecoration(labelText: 'Peso inicial (gramos)'),
                keyboardType: TextInputType.number,
                validator: (v) {
                  if (v == null || v.trim().isEmpty) return 'Ingrese el peso';
                  final n = double.tryParse(v.trim());
                  if (n == null) return 'Ingrese un número válido';
                  if (n <= 0) return 'El peso debe ser mayor a 0';
                  return null;
                },
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: _cargando ? null : _registrar,
                child: _cargando ? const CircularProgressIndicator() : const Text('Guardar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
