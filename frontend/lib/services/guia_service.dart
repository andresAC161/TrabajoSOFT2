import '../models/guia_model.dart';
import 'api_service.dart';

class GuiaService {
  final ApiService _api = ApiService();

  Future<List<Guia>> listarPorEspecie(String especie) async {
    final json = await _api.get('/guias?especie=$especie');
    return (json as List).map((e) => Guia.fromJson(e)).toList();
  }

  Future<List<Guia>> recomendadasPorLote(int loteId) async {
    final json = await _api.get('/lotes/$loteId/guias-recomendadas');
    return (json as List).map((e) => Guia.fromJson(e)).toList();
  }
}
