import '../models/registro_crecimiento_model.dart';
import 'api_service.dart';

class RegistroCrecimientoService {
  final ApiService _api = ApiService();

  Future<List<RegistroCrecimiento>> listar(int loteId) async {
    final json = await _api.get('/lotes/$loteId/crecimiento');
    return (json as List).map((e) => RegistroCrecimiento.fromJson(e)).toList();
  }

  Future<RegistroCrecimiento> registrar({
    required int loteId,
    required String pesoPromedioG,
    String? tallaCm,
    String? mortalidad,
  }) async {
    final json = await _api.post('/lotes/$loteId/crecimiento', {
      'pesoPromedioG': pesoPromedioG,
      if (tallaCm != null && tallaCm.isNotEmpty) 'tallaCm': tallaCm,
      if (mortalidad != null && mortalidad.isNotEmpty) 'mortalidad': int.parse(mortalidad),
    });
    return RegistroCrecimiento.fromJson(json);
  }
}
