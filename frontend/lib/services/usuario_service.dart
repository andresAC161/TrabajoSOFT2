import '../models/usuario_model.dart';
import 'api_service.dart';

class UsuarioService {
  final ApiService _api = ApiService();

  Future<Usuario> registrar({
    required String nombre,
    required String correo,
    required String contrasena,
    required String rol,
    String? ubicacion,
  }) async {
    final json = await _api.post('/usuarios/registrar', {
      'nombre': nombre,
      'correo': correo,
      'contrasena': contrasena,
      'rol': rol,
      if (ubicacion != null) 'ubicacion': ubicacion,
    });
    return Usuario.fromJson(json);
  }

  Future<Usuario> login({
    required String correo,
    required String contrasena,
  }) async {
    final json = await _api.post('/usuarios/login', {
      'correo': correo,
      'contrasena': contrasena,
    });
    return Usuario.fromJson(json);
  }
}
