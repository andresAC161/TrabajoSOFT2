import 'package:flutter/material.dart';
import '../models/guia_model.dart';
import '../services/guia_service.dart';

class GuiasScreen extends StatefulWidget {
  final int? loteId;
  final String especieInicial;

  const GuiasScreen({super.key, this.loteId, this.especieInicial = 'trucha'});

  @override
  State<GuiasScreen> createState() => _GuiasScreenState();
}

class _GuiasScreenState extends State<GuiasScreen> {
  final _service = GuiaService();
  final _especies = const ['trucha', 'tilapia'];

  late String _especieSeleccionada;
  List<Guia> _guias = [];
  bool _cargando = true;

  bool get _esModoAutomatico => widget.loteId != null;

  @override
  void initState() {
    super.initState();
    _especieSeleccionada = widget.especieInicial;
    _cargar();
  }

  Future<void> _cargar() async {
    setState(() => _cargando = true);
    try {
      final guias = _esModoAutomatico
          ? await _service.recomendadasPorLote(widget.loteId!)
          : await _service.listarPorEspecie(_especieSeleccionada);
      setState(() => _guias = guias);
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error al cargar guías: $e')),
        );
      }
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Guías técnicas')),
      body: Column(
        children: [
          if (_esModoAutomatico)
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              color: Colors.teal.shade50,
              child: const Text(
                'Recomendadas automáticamente según el estado reciente de tu lote.',
                style: TextStyle(color: Colors.teal),
              ),
            )
          else
            Padding(
              padding: const EdgeInsets.all(12),
              child: Row(
                children: [
                  const Text('Especie: '),
                  const SizedBox(width: 8),
                  DropdownButton<String>(
                    value: _especieSeleccionada,
                    items: _especies
                        .map((e) => DropdownMenuItem(value: e, child: Text(e)))
                        .toList(),
                    onChanged: (valor) {
                      if (valor == null) return;
                      setState(() => _especieSeleccionada = valor);
                      _cargar();
                    },
                  ),
                ],
              ),
            ),
          const Divider(height: 1),
          Expanded(
            child: _cargando
                ? const Center(child: CircularProgressIndicator())
                : _guias.isEmpty
                    ? const Center(
                        child: Text(
                          'No hay guías disponibles para esta especie.',
                          style: TextStyle(color: Colors.grey),
                        ),
                      )
                    : ListView.builder(
                        padding: const EdgeInsets.all(12),
                        itemCount: _guias.length,
                        itemBuilder: (context, index) {
                          final guia = _guias[index];
                          return Card(
                            child: ExpansionTile(
                              leading: Icon(
                                guia.categoria == 'sanidad'
                                    ? Icons.medical_services_outlined
                                    : Icons.restaurant_outlined,
                                color: Colors.teal,
                              ),
                              title: Text(guia.titulo),
                              subtitle: Text(guia.parametro == null
                                  ? '${guia.especie} · ${guia.categoria}'
                                  : '${guia.especie} · ${guia.categoria} · relacionado a ${guia.parametro}'),
                              childrenPadding:
                                  const EdgeInsets.fromLTRB(16, 0, 16, 16),
                              children: [
                                Align(
                                  alignment: Alignment.centerLeft,
                                  child: Text(guia.contenido),
                                ),
                              ],
                            ),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}
