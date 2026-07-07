import '../models/nota_model.dart';
import 'api_service.dart';

class NotaService {
  final ApiService _api = ApiService();

  Future<List<Nota>> listar(int loteId) async {
    final json = await _api.get('/lotes/$loteId/notas');
    return (json as List).map((e) => Nota.fromJson(e)).toList();
  }

  Future<Nota> agregar(int loteId, String contenido) async {
    final json = await _api.post('/lotes/$loteId/notas', {
      'contenido': contenido,
    });
    return Nota.fromJson(json);
  }
}
