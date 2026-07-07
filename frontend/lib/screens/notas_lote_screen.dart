import 'package:flutter/material.dart';
import '../models/nota_model.dart';
import '../services/nota_service.dart';

class NotasLoteScreen extends StatefulWidget {
  final int loteId;
  final String especie;
  final bool activo;

  const NotasLoteScreen({
    super.key,
    required this.loteId,
    required this.especie,
    required this.activo,
  });

  @override
  State<NotasLoteScreen> createState() => _NotasLoteScreenState();
}

class _NotasLoteScreenState extends State<NotasLoteScreen> {
  final NotaService _notaService = NotaService();
  List<Nota> _notas = [];
  bool _cargando = true;

  @override
  void initState() {
    super.initState();
    _cargar();
  }

  Future<void> _cargar() async {
    setState(() => _cargando = true);
    try {
      final notas = await _notaService.listar(widget.loteId);
      setState(() => _notas = notas);
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error al cargar notas: $e')),
        );
      }
    } finally {
      if (mounted) setState(() => _cargando = false);
    }
  }

  String _formatearFecha(String iso) {
    final fecha = DateTime.tryParse(iso);
    if (fecha == null) return iso;
    final dosDigitos = (int n) => n.toString().padLeft(2, '0');
    return '${dosDigitos(fecha.day)}/${dosDigitos(fecha.month)}/${fecha.year} '
        '${dosDigitos(fecha.hour)}:${dosDigitos(fecha.minute)}';
  }

  Future<void> _abrirDialogoNota() async {
    final controlador = TextEditingController();
    String? error;

    await showDialog<void>(
      context: context,
      builder: (dialogContext) {
        return StatefulBuilder(
          builder: (dialogContext, setDialogState) {
            return AlertDialog(
              title: const Text('Añadir nota'),
              content: TextField(
                controller: controlador,
                autofocus: true,
                maxLines: 4,
                decoration: InputDecoration(
                  hintText: 'Escribe una observación del lote...',
                  errorText: error,
                  border: const OutlineInputBorder(),
                ),
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(dialogContext),
                  child: const Text('Cancelar'),
                ),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(backgroundColor: Colors.teal),
                  onPressed: () async {
                    final texto = controlador.text.trim();
                    if (texto.isEmpty) {
                      setDialogState(
                          () => error = 'El campo es obligatorio.');
                      return;
                    }
                    Navigator.pop(dialogContext);
                    await _guardarNota(texto);
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

  Future<void> _guardarNota(String contenido) async {
    try {
      await _notaService.agregar(widget.loteId, contenido);
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Nota registrada.')),
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
      appBar: AppBar(title: Text('Notas · ${widget.especie}')),
      floatingActionButton: widget.activo
          ? FloatingActionButton.extended(
              onPressed: _abrirDialogoNota,
              backgroundColor: Colors.teal,
              icon: const Icon(Icons.add),
              label: const Text('Añadir nota'),
            )
          : null,
      body: _cargando
          ? const Center(child: CircularProgressIndicator())
          : Column(
              children: [
                if (!widget.activo)
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(12),
                    color: Colors.grey.shade200,
                    child: const Text(
                      'El lote está finalizado. Solo puedes consultar las notas.',
                      style: TextStyle(color: Colors.black54),
                    ),
                  ),
                Expanded(
                  child: _notas.isEmpty
                      ? const Center(
                          child: Text(
                            'Aún no hay observaciones registradas.',
                            style: TextStyle(color: Colors.grey),
                          ),
                        )
                      : RefreshIndicator(
                          onRefresh: _cargar,
                          child: ListView.builder(
                            padding: const EdgeInsets.all(8),
                            itemCount: _notas.length,
                            itemBuilder: (context, index) {
                              final nota = _notas[index];
                              return Card(
                                child: ListTile(
                                  leading: const Icon(Icons.sticky_note_2,
                                      color: Colors.teal),
                                  title: Text(nota.contenido),
                                  subtitle:
                                      Text(_formatearFecha(nota.fechaHora)),
                                ),
                              );
                            },
                          ),
                        ),
                ),
              ],
            ),
    );
  }
}
