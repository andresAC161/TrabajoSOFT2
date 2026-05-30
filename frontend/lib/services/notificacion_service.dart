import '../models/notificacion_model.dart';
import 'api_service.dart';

class NotificacionService {
  final ApiService _api = ApiService();

  Future<List<Notificacion>> listar(int usuarioId) async {
    final json = await _api.get('/notificaciones/$usuarioId');
    return (json as List).map((e) => Notificacion.fromJson(e)).toList();
  }

  Future<Notificacion> marcarLeida(int id) async {
    final json = await _api.patch('/notificaciones/$id/leida');
    return Notificacion.fromJson(json);
  }
}
