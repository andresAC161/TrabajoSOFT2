import 'package:flutter/material.dart';
import '../models/notificacion_model.dart';
import '../services/notificacion_service.dart';

class NotificacionesScreen extends StatefulWidget {
  final int usuarioId;
  const NotificacionesScreen({super.key, required this.usuarioId});

  @override
  State<NotificacionesScreen> createState() => _NotificacionesScreenState();
}

class _NotificacionesScreenState extends State<NotificacionesScreen> {
  final _service = NotificacionService();
  List<Notificacion> _items = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _cargar();
  }

  Future<void> _cargar() async {
    setState(() => _cargando = true);
    try {
      final items = await _service.listar(widget.usuarioId);
      setState(() => _items = items);
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(e.toString())));
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  Future<void> _marcarLeida(Notificacion n) async {
    if (n.leida) return;
    try {
      final actualizada = await _service.marcarLeida(n.id);
      setState(() {
        final idx = _items.indexWhere((e) => e.id == n.id);
        if (idx != -1) _items[idx] = actualizada;
      });
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(e.toString())));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Notificaciones'),
        actions: [
          IconButton(icon: const Icon(Icons.refresh), onPressed: _cargar),
        ],
      ),
      body: _cargando
          ? const Center(child: CircularProgressIndicator())
          : _items.isEmpty
              ? const Center(child: Text('Sin notificaciones'))
              : RefreshIndicator(
                  onRefresh: _cargar,
                  child: ListView.builder(
                    itemCount: _items.length,
                    itemBuilder: (_, i) {
                      final n = _items[i];
                      return ListTile(
                        leading: Icon(
                          n.leida ? Icons.notifications_none : Icons.notifications_active,
                          color: n.leida ? Colors.grey : Colors.orange,
                        ),
                        title: Text(
                          n.mensaje,
                          style: TextStyle(
                            fontWeight: n.leida ? FontWeight.normal : FontWeight.bold,
                          ),
                        ),
                        subtitle: Text(n.fechaEnvio.substring(0, 16).replaceAll('T', ' ')),
                        onTap: () => _marcarLeida(n),
                      );
                    },
                  ),
                ),
    );
  }
}
