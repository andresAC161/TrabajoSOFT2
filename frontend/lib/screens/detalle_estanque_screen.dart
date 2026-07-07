import 'package:flutter/material.dart';
import '../models/estanque_model.dart';
import '../models/lote_model.dart';
import '../models/parametro_agua_model.dart';
import '../services/estanque_service.dart';
import '../services/lote_service.dart';
import '../services/parametro_agua_service.dart';
import 'editar_estanque_screen.dart';
import 'notas_lote_screen.dart';
import 'registrar_lote_screen.dart';
import 'registrar_parametros_screen.dart';

class DetalleEstanqueScreen extends StatefulWidget {
  final int estanqueId;
  final int usuarioId;
  const DetalleEstanqueScreen({super.key, required this.estanqueId, required this.usuarioId});

  @override
  State<DetalleEstanqueScreen> createState() => _DetalleEstanqueScreenState();
}

class _DetalleEstanqueScreenState extends State<DetalleEstanqueScreen> {
  final _estanqueService = EstanqueService();
  final _loteService = LoteService();
  final _parametroService = ParametroAguaService();

  Estanque? _estanque;
  List<Lote> _lotes = [];
  List<ParametroAgua> _parametros = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _cargar();
  }

  Future<void> _cargar() async {
    setState(() => _cargando = true);
    try {
      final results = await Future.wait([
        _estanqueService.buscar(widget.estanqueId),
        _loteService.listar(widget.estanqueId),
        _parametroService.listar(widget.estanqueId),
      ]);
      setState(() {
        _estanque = results[0] as Estanque;
        _lotes = results[1] as List<Lote>;
        _parametros = results[2] as List<ParametroAgua>;
      });
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(e.toString())));
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  Future<void> _finalizarLote(Lote lote) async {
    final confirmar = await showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Finalizar lote'),
        content: Text(
          '¿Confirma la cosecha del lote de ${lote.especie}?\n\n'
          'El lote quedará marcado como finalizado y el estanque '
          'quedará disponible para nuevas siembras.',
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancelar')),
          ElevatedButton(
            onPressed: () => Navigator.pop(context, true),
            style: ElevatedButton.styleFrom(backgroundColor: Colors.teal),
            child: const Text('Confirmar cosecha'),
          ),
        ],
      ),
    );
    if (confirmar != true) return;
    try {
      await _loteService.finalizar(lote.id);
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Lote finalizado. Estanque liberado correctamente.')),
      );
      _cargar();
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(e.toString().replaceAll('Exception: ', ''))),
      );
    }
  }

  void _abrirNotas(Lote lote) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => NotasLoteScreen(
          loteId: lote.id,
          especie: lote.especie,
          activo: lote.estado == 'activo',
        ),
      ),
    );
  }

  Future<void> _eliminar() async {
    final confirmar = await showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Eliminar estanque'),
        content: const Text('¿Desea eliminar este estanque?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancelar')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('Eliminar')),
        ],
      ),
    );
    if (confirmar != true) return;
    try {
      await _estanqueService.eliminar(widget.estanqueId);
      if (!mounted) return;
      Navigator.pop(context, true);
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(e.toString())));
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_cargando) return const Scaffold(body: Center(child: CircularProgressIndicator()));
    if (_estanque == null) return const Scaffold(body: Center(child: Text('No encontrado')));

    return Scaffold(
      appBar: AppBar(
        title: Text(_estanque!.nombre),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: () async {
              final editado = await Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => EditarEstanqueScreen(estanque: _estanque!),
                ),
              );
              if (editado == true) _cargar();
            },
          ),
          IconButton(icon: const Icon(Icons.delete), onPressed: _eliminar),
        ],
      ),
      body: RefreshIndicator(
        onRefresh: _cargar,
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            _InfoCard(estanque: _estanque!),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text('Lotes', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                TextButton.icon(
                  icon: const Icon(Icons.add),
                  label: const Text('Nuevo lote'),
                  onPressed: () async {
                    final ok = await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => RegistrarLoteScreen(estanqueId: widget.estanqueId),
                      ),
                    );
                    if (ok == true) _cargar();
                  },
                ),
              ],
            ),
            ..._lotes.map((l) => Card(
                  child: ListTile(
                    leading: Icon(
                      l.estado == 'finalizado' ? Icons.check_circle : Icons.set_meal,
                      color: l.estado == 'finalizado' ? Colors.grey : Colors.teal,
                    ),
                    title: Text(l.especie),
                    subtitle: Text('${l.cantidad} peces · ${l.estado}'
                        '${l.fechaFin != null ? ' · Fin: ${l.fechaFin}' : ''}'),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(
                          icon: const Icon(Icons.sticky_note_2_outlined),
                          tooltip: 'Notas',
                          color: Colors.teal,
                          onPressed: () => _abrirNotas(l),
                        ),
                        if (l.estado == 'activo')
                          TextButton(
                            onPressed: () => _finalizarLote(l),
                            child: const Text('Finalizar', style: TextStyle(color: Colors.red)),
                          ),
                      ],
                    ),
                    onTap: () => _abrirNotas(l),
                  ),
                )),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text('Parámetros de agua',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                TextButton.icon(
                  icon: const Icon(Icons.add),
                  label: const Text('Registrar'),
                  onPressed: () async {
                    final ok = await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => RegistrarParametrosScreen(
                          estanqueId: widget.estanqueId,
                          usuarioId: widget.usuarioId,
                        ),
                      ),
                    );
                    if (ok == true) _cargar();
                  },
                ),
              ],
            ),
            ..._parametros.take(5).map((p) => ListTile(
                  title: Text('T: ${p.temperaturaC}°C  pH: ${p.ph}  O₂: ${p.oxigenoMgl} mg/L  NH₃: ${p.amoniacoMgl}'),
                  subtitle: Text(p.fechaRegistro.substring(0, 10)),
                )),
          ],
        ),
      ),
    );
  }
}

class _InfoCard extends StatelessWidget {
  final Estanque estanque;
  const _InfoCard({required this.estanque});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Tipo de agua: ${estanque.tipoAgua}'),
            Text('Capacidad: ${estanque.capacidadLitros} L'),
            Text('Estado: ${estanque.estado}'),
            if (estanque.localizacion != null) Text('Localización: ${estanque.localizacion}'),
          ],
        ),
      ),
    );
  }
}
