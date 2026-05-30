import '../models/lote_model.dart';
import 'api_service.dart';

class LoteService {
  final ApiService _api = ApiService();

  Future<Lote> registrar({
    required int estanqueId,
    required String especie,
    required int cantidad,
    required String fechaSiembra,
    required String pesoInicialG,
  }) async {
    final json = await _api.post('/lotes', {
      'estanqueId': estanqueId,
      'especie': especie,
      'cantidad': cantidad,
      'fechaSiembra': fechaSiembra,
      'pesoInicialG': pesoInicialG,
    });
    return Lote.fromJson(json);
  }

  Future<List<Lote>> listar(int estanqueId) async {
    final json = await _api.get('/lotes?estanqueId=$estanqueId');
    return (json as List).map((e) => Lote.fromJson(e)).toList();
  }
}
