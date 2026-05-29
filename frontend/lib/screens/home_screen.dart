import 'dart:async';
import 'package:flutter/material.dart';
import '../models/estanque_model.dart';
import '../models/notificacion_model.dart';
import '../models/usuario_model.dart';
import '../services/estanque_service.dart';
import '../services/notificacion_service.dart';
import 'bienvenida_screen.dart';
import 'detalle_estanque_screen.dart';
import 'notificaciones_screen.dart';
import 'programar_tarea_screen.dart';
import 'registrar_estanque_screen.dart';
import 'registrar_lote_screen.dart';
import 'registrar_parametros_screen.dart';
import 'registro_screen.dart';

class HomeScreen extends StatefulWidget {
  final Usuario usuario;
  const HomeScreen({super.key, required this.usuario});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final _estanqueService = EstanqueService();
  final _notificacionService = NotificacionService();
  List<Estanque> _estanques = [];
  bool _cargando = true;
  int _notificacionesNoLeidas = 0;
  Timer? _timer;

  Future<int?> _elegirEstanque(String titulo) async {
    return showDialog<int>(
      context: context,
      builder: (_) => SimpleDialog(
        title: Text(titulo),
        children: _estanques.map((e) => SimpleDialogOption(
          onPressed: () => Navigator.pop(context, e.id),
          child: Text('${e.nombre}  ·  ${e.estado}'),
        )).toList(),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    _cargarEstanques();
    _verificarNotificaciones();
    _timer = Timer.periodic(const Duration(seconds: 30), (_) => _verificarNotificaciones());
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  Future<void> _verificarNotificaciones() async {
    try {
      final lista = await _notificacionService.listar(widget.usuario.id);
      final noLeidas = lista.where((n) => !n.leida).toList();
      final previas = _notificacionesNoLeidas;
      setState(() => _notificacionesNoLeidas = noLeidas.length);
      if (noLeidas.isNotEmpty && noLeidas.length > previas && mounted) {
        final ultima = noLeidas.first;
        _mostrarAlerta(ultima);
      }
    } catch (_) {}
  }

  void _mostrarAlerta(Notificacion n) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: const Row(
          children: [
            Icon(Icons.notifications_active, color: Colors.orange),
            SizedBox(width: 8),
            Text('Tarea próxima'),
          ],
        ),
        content: Text(n.mensaje),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Ver notificaciones'),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => NotificacionesScreen(usuarioId: widget.usuario.id),
                ),
              );
            },
            child: const Text('Abrir'),
          ),
        ],
      ),
    );
  }

  Future<void> _cargarEstanques() async {
    setState(() => _cargando = true);
    try {
      final lista = await _estanqueService.listar(widget.usuario.id);
      setState(() => _estanques = lista);
    } catch (_) {
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  void _cerrarSesion() {
    Navigator.pushAndRemoveUntil(
      context,
      MaterialPageRoute(builder: (_) => const BienvenidaScreen()),
      (_) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Hola, ${widget.usuario.nombre.split(' ').first}'),
        actions: [
          Stack(
            children: [
              IconButton(
                icon: const Icon(Icons.notifications),
                onPressed: () async {
                  await Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => NotificacionesScreen(usuarioId: widget.usuario.id),
                    ),
                  );
                  _verificarNotificaciones();
                },
              ),
              if (_notificacionesNoLeidas > 0)
                Positioned(
                  right: 8,
                  top: 8,
                  child: Container(
                    padding: const EdgeInsets.all(2),
                    decoration: const BoxDecoration(
                      color: Colors.red,
                      shape: BoxShape.circle,
                    ),
                    constraints: const BoxConstraints(minWidth: 16, minHeight: 16),
                    child: Text(
                      '$_notificacionesNoLeidas',
                      style: const TextStyle(color: Colors.white, fontSize: 10),
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
            ],
          ),
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: _cerrarSesion,
          ),
        ],
      ),
      body: RefreshIndicator(
        onRefresh: _cargarEstanques,
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            _SectionTitle('Módulos'),
            _MenuTile(
              icon: Icons.water,
              label: 'Registrar estanque',
              onTap: () async {
                final ok = await Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => RegistrarEstanqueScreen(usuarioId: widget.usuario.id),
                  ),
                );
                if (ok == true) _cargarEstanques();
              },
            ),
            _MenuTile(
              icon: Icons.task,
              label: 'Programar tarea',
              onTap: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => ProgramarTareaScreen(usuarioId: widget.usuario.id),
                ),
              ),
            ),
            if (_estanques.isNotEmpty) ...[
              _MenuTile(
                icon: Icons.science,
                label: 'Registrar parámetros de agua',
                onTap: () async {
                  final id = await _elegirEstanque('Selecciona un estanque');
                  if (id == null || !mounted) return;
                  Navigator.push(context, MaterialPageRoute(
                    builder: (_) => RegistrarParametrosScreen(estanqueId: id, usuarioId: widget.usuario.id),
                  ));
                },
              ),
              _MenuTile(
                icon: Icons.set_meal,
                label: 'Registrar lote',
                onTap: () async {
                  final id = await _elegirEstanque('Selecciona un estanque');
                  if (id == null || !mounted) return;
                  Navigator.push(context, MaterialPageRoute(
                    builder: (_) => RegistrarLoteScreen(estanqueId: id),
                  ));
                },
              ),
            ],
            const SizedBox(height: 24),
            _SectionTitle('Mis estanques'),
            if (_cargando)
              const Center(child: CircularProgressIndicator())
            else if (_estanques.isEmpty)
              const Padding(
                padding: EdgeInsets.symmetric(vertical: 16),
                child: Text('Sin estanques registrados. Registra uno primero.'),
              )
            else
              ..._estanques.map(
                (e) => Card(
                  child: ListTile(
                    leading: const Icon(Icons.water),
                    title: Text(e.nombre),
                    subtitle: Text('${e.tipoAgua} · ${e.estado}'),
                    trailing: const Icon(Icons.chevron_right),
                    onTap: () async {
                      await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => DetalleEstanqueScreen(estanqueId: e.id, usuarioId: widget.usuario.id),
                        ),
                      );
                      _cargarEstanques();
                    },
                  ),
                ),
              ),
          ],
        ),
      ),
    );
  }
}

class _SectionTitle extends StatelessWidget {
  final String text;
  const _SectionTitle(this.text);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Text(text, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
    );
  }
}

class _MenuTile extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback onTap;
  const _MenuTile({required this.icon, required this.label, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        leading: Icon(icon, color: Theme.of(context).colorScheme.primary),
        title: Text(label),
        trailing: const Icon(Icons.chevron_right),
        onTap: onTap,
      ),
    );
  }
}
