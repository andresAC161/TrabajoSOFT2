import 'package:flutter/material.dart';
import 'screens/bienvenida_screen.dart';

void main() {
  runApp(const PezcasesorApp());
}

class PezcasesorApp extends StatelessWidget {
  const PezcasesorApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Pezcasesor',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorSchemeSeed: Colors.teal,
        useMaterial3: true,
      ),
      home: const BienvenidaScreen(),
    );
  }
}
