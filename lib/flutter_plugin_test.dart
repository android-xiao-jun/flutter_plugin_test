import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginTest {
  static const MethodChannel _channel =
      const MethodChannel('flutter_plugin_test');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String>  platformFullSreen(bool b) async {
    Map<String, dynamic> map = {"full": b};
    final String version = await _channel.invokeMethod('fullscreen',map);
    return version;
  }
}
