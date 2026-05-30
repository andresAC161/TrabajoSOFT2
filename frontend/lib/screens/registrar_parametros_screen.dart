import 'package:flutter/material.dart';
import '../services/parametro_agua_service.dart';

class RegistrarParametrosScreen extends StatefulWidget {
  final int estanqueId;
  final int usuarioId;
  const RegistrarParametrosScreen({
    super.key,
    required this.estanqueId,
    required this.usuarioId,
  });

  @override
  State<RegistrarParametrosScreen> createState() => _RegistrarParametrosScreenState();
}

class _RegistrarParametrosScreenState extends State<RegistrarParametrosScreen> {
  final _formKey = GlobalKey<FormState>();
  final _tempCtrl = TextEditingController();
  final _phCtrl = TextEditingController();
  final _o2Ctrl = TextEditingController();
  final _nh3Ctrl = TextEditingController();
  bool _cargando = false;

  final _service = ParametroAguaService();

  String? _validarPositivo(String? v, String campo) {
    if (v == null || v.trim().isEmpty) return 'Ingrese $campo';
    final n = double.tryParse(v.trim());
    if (n == null) return 'Ingrese un número válido en $campo';
    if (n <= 0) return '$campo debe ser mayor a 0';
    return null;
  }

  Future<void> _registrar() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _cargando = true);
    try {
      await _service.registrar(
        estanqueId: widget.estanqueId,
        usuarioId: widget.usuarioId,
        temperaturaC: _tempCtrl.text.trim(),
        ph: _phCtrl.text.trim(),
        oxigenoMgl: _o2Ctrl.text.trim(),
        amoniacoMgl: _nh3Ctrl.text.trim(),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Parámetros registrados correctamente')),
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
      appBar: AppBar(title: const Text('Registrar parámetros de agua')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _tempCtrl,
                decoration: const InputDecoration(
                  labelText: 'Temperatura (°C) *',
                  hintText: 'ej: 15.5',
                ),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                validator: (v) => _validarPositivo(v, 'temperatura'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _phCtrl,
                decoration: const InputDecoration(
                  labelText: 'pH *',
                  hintText: 'ej: 7.4',
                ),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                validator: (v) => _validarPositivo(v, 'pH'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _o2Ctrl,
                decoration: const InputDecoration(
                  labelText: 'Oxígeno disuelto (mg/L) *',
                  hintText: 'ej: 6.8',
                ),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                validator: (v) => _validarPositivo(v, 'oxígeno disuelto'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _nh3Ctrl,
                decoration: const InputDecoration(
                  labelText: 'Amoníaco (mg/L) *',
                  hintText: 'ej: 0.02',
                ),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                validator: (v) => _validarPositivo(v, 'amoníaco'),
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
