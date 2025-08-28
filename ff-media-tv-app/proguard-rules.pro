# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}


# Keep all Amazon IAP SDK classes
-keep class com.amazon.** { *; }
-dontwarn com.amazon.**
# Keep interfaces and their implementations
-keep interface com.amazon.a.a.k.b { *; }
-keep class com.amazon.a.a.k.c.** { *; }
-keep class com.amazon.device.iap.** { *; }
# Prevent obfuscation of classes that may be used via reflection
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod