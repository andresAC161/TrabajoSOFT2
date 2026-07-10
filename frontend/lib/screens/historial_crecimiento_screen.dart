import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import '../models/registro_crecimiento_model.dart';
import '../services/registro_crecimiento_service.dart';

class HistorialCrecimientoScreen extends StatefulWidget {
  final int loteId;
  final String especie;

  const HistorialCrecimientoScreen({
    super.key,
    required this.loteId,
    required this.especie,
  });

  @override
  State<HistorialCrecimientoScreen> createState() => _HistorialCrecimientoScreenState();
}

class _HistorialCrecimientoScreenState extends State<HistorialCrecimientoScreen> {
  final _service = RegistroCrecimientoService();
  List<RegistroCrecimiento> _registros = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _cargar();
  }

  Future<void> _cargar() async {
    setState(() => _cargando = true);
    try {
      final registros = await _service.listar(widget.loteId);
      setState(() => _registros = registros);
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error al cargar historial: $e')),
        );
      }
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  Future<void> _abrirDialogoMuestreo() async {
    final pesoCtrl = TextEditingController();
    final tallaCtrl = TextEditingController();
    final mortalidadCtrl = TextEditingController();
    String? errorPeso;
    String? errorTalla;
    String? errorMortalidad;

    await showDialog<void>(
      context: context,
      builder: (dialogContext) {
        return StatefulBuilder(
          builder: (dialogContext, setDialogState) {
            return AlertDialog(
              title: const Text('Nuevo muestreo'),
              content: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextField(
                    controller: pesoCtrl,
                    keyboardType: const TextInputType.numberWithOptions(decimal: true, signed: true),
                    decoration: InputDecoration(
                      labelText: 'Peso promedio (g) *',
                      errorText: errorPeso,
                    ),
                  ),
                  TextField(
                    controller: tallaCtrl,
                    keyboardType: const TextInputType.numberWithOptions(decimal: true, signed: true),
                    decoration: InputDecoration(
                      labelText: 'Talla (cm)',
                      errorText: errorTalla,
                    ),
                  ),
                  TextField(
                    controller: mortalidadCtrl,
                    keyboardType: const TextInputType.numberWithOptions(signed: true),
                    decoration: InputDecoration(
                      labelText: 'Mortalidad (peces)',
                      errorText: errorMortalidad,
                    ),
                  ),
                ],
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(dialogContext),
                  child: const Text('Cancelar'),
                ),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(backgroundColor: Colors.teal),
                  onPressed: () async {
                    final peso = double.tryParse(pesoCtrl.text.trim());
                    final tallaTexto = tallaCtrl.text.trim();
                    final talla = tallaTexto.isEmpty ? null : double.tryParse(tallaTexto);
                    final mortalidadTexto = mortalidadCtrl.text.trim();
                    final mortalidad = mortalidadTexto.isEmpty ? null : int.tryParse(mortalidadTexto);

                    setDialogState(() {
                      errorPeso = (peso == null || peso <= 0) ? 'Ingrese un peso válido' : null;
                      errorTalla = (tallaTexto.isNotEmpty && (talla == null || talla <= 0))
                          ? 'Ingrese una talla válida'
                          : null;
                      errorMortalidad = (mortalidadTexto.isNotEmpty && (mortalidad == null || mortalidad < 0))
                          ? 'La mortalidad no puede ser negativa'
                          : null;
                    });
                    if (errorPeso != null || errorTalla != null || errorMortalidad != null) {
                      return;
                    }
                    Navigator.pop(dialogContext);
                    await _guardarMuestreo(
                      pesoCtrl.text.trim(),
                      tallaCtrl.text.trim(),
                      mortalidadCtrl.text.trim(),
                    );
                  },
                  child: const Text('Guardar'),
                ),
              ],
            );
          },
        );
      },
    );
  }

  Future<void> _guardarMuestreo(String peso, String talla, String mortalidad) async {
    try {
      await _service.registrar(
        loteId: widget.loteId,
        pesoPromedioG: peso,
        tallaCm: talla,
        mortalidad: mortalidad,
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Muestreo registrado.')),
      );
      _cargar();
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(e.toString().replaceAll('Exception: ', ''))),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Crecimiento · ${widget.especie}')),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _abrirDialogoMuestreo,
        backgroundColor: Colors.teal,
        icon: const Icon(Icons.add),
        label: const Text('Nuevo muestreo'),
      ),
      body: _cargando
          ? const Center(child: CircularProgressIndicator())
          : _registros.isEmpty
              ? const Center(
                  child: Text(
                    'Aún no hay muestreos registrados para este lote.',
                    style: TextStyle(color: Colors.grey),
                  ),
                )
              : RefreshIndicator(
                  onRefresh: _cargar,
                  child: ListView(
                    padding: const EdgeInsets.all(16),
                    children: [
                      SizedBox(height: 220, child: _GraficoCrecimiento(registros: _registros)),
                      const SizedBox(height: 16),
                      const Text('Muestreos', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                      const SizedBox(height: 8),
                      ..._registros.map((r) => Card(
                            child: ListTile(
                              leading: const Icon(Icons.monitor_weight, color: Colors.teal),
                              title: Text('${r.pesoPromedioG} g'
                                  '${r.tallaCm != null ? ' · ${r.tallaCm} cm' : ''}'
                                  '${r.mortalidad != null ? ' · ${r.mortalidad} muertes' : ''}'),
                              subtitle: Text(r.fechaMuestreo),
                            ),
                          )),
                    ],
                  ),
                ),
    );
  }
}

class _GraficoCrecimiento extends StatelessWidget {
  final List<RegistroCrecimiento> registros;
  const _GraficoCrecimiento({required this.registros});

  @override
  Widget build(BuildContext context) {
    final puntos = List.generate(
      registros.length,
      (i) => FlSpot(i.toDouble(), registros[i].pesoPromedioG),
    );

    return LineChart(
      LineChartData(
        gridData: const FlGridData(show: true),
        titlesData: FlTitlesData(
          leftTitles: AxisTitles(
            sideTitles: SideTitles(showTitles: true, reservedSize: 40),
          ),
          bottomTitles: AxisTitles(
            sideTitles: SideTitles(
              showTitles: true,
              getTitlesWidget: (value, meta) {
                final i = value.toInt();
                if (i < 0 || i >= registros.length) return const SizedBox.shrink();
                return Padding(
                  padding: const EdgeInsets.only(top: 4),
                  child: Text(
                    registros[i].fechaMuestreo.substring(5),
                    style: const TextStyle(fontSize: 10),
                  ),
                );
              },
            ),
          ),
          rightTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
          topTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
        ),
        borderData: FlBorderData(show: true),
        lineBarsData: [
          LineChartBarData(
            spots: puntos,
            isCurved: false,
            color: Colors.teal,
            barWidth: 3,
            dotData: const FlDotData(show: true),
          ),
        ],
      ),
    );
  }
}
