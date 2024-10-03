package io.willmack.flutter_device_friendly_name;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

import android.provider.Settings;
import android.content.Context;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterDeviceFriendlyNamePlugin
 */
public class FlutterDeviceFriendlyNamePlugin implements FlutterPlugin, MethodCallHandler {  /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;

    /**
     * Plugin registration.
     */
    public static void registerWith(@NonNull Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_device_friendly_name");
        channel.setMethodCallHandler(new FlutterDeviceFriendlyNamePlugin(registrar.context()));
    }

    public FlutterDeviceFriendlyNamePlugin() {
    }

    public FlutterDeviceFriendlyNamePlugin(Context context) {
        this.context = context;
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "flutter_device_friendly_name");
        channel.setMethodCallHandler(this);
        context = binding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getDeviceFriendlyName")) {
            String friendlyName = null;
            if (android.os.Build.VERSION.SDK_INT <= 31) {
                friendlyName = Settings.Secure.getString(context.getContentResolver(), "bluetooth_name");
            }
            if ((friendlyName == null) || friendlyName.isEmpty()) {
                friendlyName = Settings.Global.getString(context.getContentResolver(), "device_name");
            }
            result.success(friendlyName);
        } else {
            result.notImplemented();
        }
    }
}
