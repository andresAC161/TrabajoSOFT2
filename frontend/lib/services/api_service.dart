import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiService {
  static final ApiService _instance = ApiService._internal();
  factory ApiService() => _instance;
  ApiService._internal();

  static const String baseUrl = 'http://localhost:8080/api';

  Map<String, String> get _headers => {'Content-Type': 'application/json'};

  Future<Map<String, dynamic>> post(String path, Map<String, dynamic> body) async {
    final response = await http.post(
      Uri.parse('$baseUrl$path'),
      headers: _headers,
      body: jsonEncode(body),
    );
    _checkStatus(response);
    return jsonDecode(response.body);
  }

  Future<Map<String, dynamic>> put(String path, Map<String, dynamic> body) async {
    final response = await http.put(
      Uri.parse('$baseUrl$path'),
      headers: _headers,
      body: jsonEncode(body),
    );
    _checkStatus(response);
    return jsonDecode(response.body);
  }

  Future<void> delete(String path) async {
    final response = await http.delete(Uri.parse('$baseUrl$path'), headers: _headers);
    _checkStatus(response);
  }

  Future<dynamic> get(String path) async {
    final response = await http.get(Uri.parse('$baseUrl$path'), headers: _headers);
    _checkStatus(response);
    return jsonDecode(response.body);
  }

  Future<Map<String, dynamic>> patch(String path) async {
    final response = await http.patch(Uri.parse('$baseUrl$path'), headers: _headers);
    _checkStatus(response);
    return jsonDecode(response.body);
  }

  void _checkStatus(http.Response response) {
    if (response.statusCode >= 400) {
      String msg = 'Error ${response.statusCode}';
      try {
        final body = jsonDecode(response.body);
        msg = body['message'] ?? body['error'] ?? msg;
      } catch (_) {}
      throw Exception(msg);
    }
  }
}
