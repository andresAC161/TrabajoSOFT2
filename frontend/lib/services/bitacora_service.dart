import '../models/bitacora_model.dart';
import 'api_service.dart';

class BitacoraService {
  final ApiService _api = ApiService();

  Future<List<Bitacora>> listar() async {
    final json = await _api.get('/historial');
    return (json as List).map((e) => Bitacora.fromJson(e)).toList();
  }
}
