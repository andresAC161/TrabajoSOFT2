import '../models/tarea_model.dart';
import 'api_service.dart';

class TareaService {
  final ApiService _api = ApiService();

  Future<Tarea> crear({
    required int usuarioId,
    int? estanqueId,
    int? loteId,
    required String nombre,
    String? descripcion,
    required String fechaHora,
  }) async {
    final json = await _api.post('/tareas', {
      'usuarioId': usuarioId,
      if (estanqueId != null) 'estanqueId': estanqueId,
      if (loteId != null) 'loteId': loteId,
      'nombre': nombre,
      if (descripcion != null) 'descripcion': descripcion,
      'fechaHora': fechaHora,
    });
    return Tarea.fromJson(json);
  }

  Future<List<Tarea>> listar(int usuarioId) async {
    final json = await _api.get('/tareas?usuarioId=$usuarioId');
    return (json as List).map((e) => Tarea.fromJson(e)).toList();
  }

  Future<Tarea> actualizar(int id, Map<String, dynamic> campos) async {
    final json = await _api.put('/tareas/$id', campos);
    return Tarea.fromJson(json);
  }
}
