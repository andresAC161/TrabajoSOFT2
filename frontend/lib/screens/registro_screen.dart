import 'package:flutter/material.dart';
import '../services/usuario_service.dart';

class RegistroScreen extends StatefulWidget {
  const RegistroScreen({super.key});

  @override
  State<RegistroScreen> createState() => _RegistroScreenState();
}

class _RegistroScreenState extends State<RegistroScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nombreCtrl = TextEditingController();
  final _correoCtrl = TextEditingController();
  final _contrasenaCtrl = TextEditingController();
  final _ubicacionCtrl = TextEditingController();
  String _rol = 'productor';
  bool _cargando = false;

  final _service = UsuarioService();

  Future<void> _registrar() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _cargando = true);
    try {
      await _service.registrar(
        nombre: _nombreCtrl.text.trim(),
        correo: _correoCtrl.text.trim(),
        contrasena: _contrasenaCtrl.text,
        rol: _rol,
        ubicacion: _ubicacionCtrl.text.trim().isEmpty
            ? null
            : _ubicacionCtrl.text.trim(),
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
            content: Text('Usuario registrado. Ahora inicia sesión.')),
      );
      Navigator.pop(context);
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
      appBar: AppBar(title: const Text('Registrar usuario')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nombreCtrl,
                decoration:
                    const InputDecoration(labelText: 'Nombre completo *'),
                validator: (v) =>
                    v == null || v.trim().isEmpty ? 'Ingrese su nombre' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _correoCtrl,
                decoration:
                    const InputDecoration(labelText: 'Correo electrónico *'),
                keyboardType: TextInputType.emailAddress,
                validator: (v) {
                  if (v == null || v.trim().isEmpty) return 'Ingrese su correo';
                  final emailRegex =
                      RegExp(r'^[\w\.\-]+@[\w\-]+\.[a-zA-Z]{2,}$');
                  if (!emailRegex.hasMatch(v.trim()))
                    return 'Formato de correo inválido';
                  return null;
                },
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _contrasenaCtrl,
                decoration: const InputDecoration(
                    labelText: 'Contraseña * (mínimo 8 caracteres)'),
                obscureText: true,
                validator: (v) =>
                    v == null || v.length < 8 ? 'Mínimo 8 caracteres' : null,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: _rol,
                decoration: const InputDecoration(labelText: 'Rol *'),
                items: const [
                  DropdownMenuItem(
                      value: 'productor', child: Text('Productor')),
                  DropdownMenuItem(value: 'tecnico', child: Text('Técnico')),
                  DropdownMenuItem(
                      value: 'administrador', child: Text('Administrador')),
                ],
                onChanged: (v) => setState(() => _rol = v!),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _ubicacionCtrl,
                decoration:
                    const InputDecoration(labelText: 'Ubicación (opcional)'),
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: _cargando ? null : _registrar,
                child: _cargando
                    ? const CircularProgressIndicator()
                    : const Text('Registrar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
