import '../models/parametro_agua_model.dart';
import 'api_service.dart';

class ParametroAguaService {
  final ApiService _api = ApiService();

  Future<ParametroAgua> registrar({
    required int estanqueId,
    required int usuarioId,
    required String temperaturaC,
    required String ph,
    required String oxigenoMgl,
    required String amoniacoMgl,
  }) async {
    final json = await _api.post('/parametros-agua', {
      'estanqueId': estanqueId,
      'usuarioId': usuarioId,
      'temperaturaC': temperaturaC,
      'ph': ph,
      'oxigenoMgl': oxigenoMgl,
      'amoniacoMgl': amoniacoMgl,
    });
    return ParametroAgua.fromJson(json);
  }

  Future<List<ParametroAgua>> listar(int estanqueId) async {
    final json = await _api.get('/parametros-agua/$estanqueId');
    return (json as List).map((e) => ParametroAgua.fromJson(e)).toList();
  }
}
