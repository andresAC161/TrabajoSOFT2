import '../models/estanque_model.dart';
import 'api_service.dart';

class EstanqueService {
  final ApiService _api = ApiService();

  Future<Estanque> registrar({
    required int usuarioId,
    required String nombre,
    required String tipoAgua,
    required String capacidadLitros,
    String? localizacion,
  }) async {
    final json = await _api.post('/estanques', {
      'usuarioId': usuarioId,
      'nombre': nombre,
      'tipoAgua': tipoAgua,
      'capacidadLitros': capacidadLitros,
      if (localizacion != null) 'localizacion': localizacion,
    });
    return Estanque.fromJson(json);
  }

  Future<List<Estanque>> listar(int usuarioId) async {
    final json = await _api.get('/estanques?usuarioId=$usuarioId');
    return (json as List).map((e) => Estanque.fromJson(e)).toList();
  }

  Future<Estanque> buscar(int id) async {
    final json = await _api.get('/estanques/$id');
    return Estanque.fromJson(json);
  }

  Future<Estanque> editar(int id, Map<String, dynamic> campos) async {
    final json = await _api.put('/estanques/$id', campos);
    return Estanque.fromJson(json);
  }

  Future<void> eliminar(int id) async {
    await _api.delete('/estanques/$id');
  }
}
