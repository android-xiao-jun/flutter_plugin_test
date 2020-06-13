package com.example.flutter_plugin_test

import android.util.Log
import android.view.Window
import android.view.WindowManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** FlutterPluginTestPlugin */
public class FlutterPluginTestPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_plugin_test")
        channel.setMethodCallHandler(this);
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    companion object {
        lateinit var registrar1: Registrar;

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            registrar1 = registrar;
            val channel = MethodChannel(registrar.messenger(), "flutter_plugin_test")
            channel.setMethodCallHandler(FlutterPluginTestPlugin())
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {

            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "fullscreen") {
            var argument: Boolean? = call.argument<Boolean>("full")
            Log.e("fullscreen",""+argument)
            if (argument != null) {
                full(argument, registrar1.activity().window)
                result.success("设置成功")
            }else{
                result.success("设置失败")
            }
        }else if(call.method == "bar_status"){
            var argument: Boolean? = call.argument<Boolean>("drak")
            if (argument != null) {
                //低版本状态栏颜色问题
                SetStatusBarLightModeUtils.set5_1BarTextColor(registrar1.activity(),argument);
                result.success("设置成功")
            }else{
                result.success("设置失败")
            }
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    //是否全屏
    private fun full(enable: Boolean, window: Window) {
        if (enable) {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN or localLayoutParams.flags
            window.attributes = localLayoutParams
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN.inv() and localLayoutParams.flags// 移除flag
            window.attributes = localLayoutParams
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}
