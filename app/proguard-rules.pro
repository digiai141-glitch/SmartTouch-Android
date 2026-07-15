# SmartTouch ProGuard Rules

# Keep application class
-keep class com.smarttouch.app.SmartTouchApp { *; }

# Keep all data models
-keep class com.smarttouch.app.data.model.** { *; }

# Keep service classes
-keep class com.smarttouch.app.service.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# DataStore
-keep class androidx.datastore.** { *; }
-keep class com.google.protobuf.** { *; }

# Compose
-keep class androidx.compose.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}
