# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Keep source file names for debugging
-renamesourcefileattribute SourceFile

# ===============================================================================
# Dagger Hilt
# ===============================================================================
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }
-keep @dagger.hilt.InstallIn class *
-keep @dagger.hilt.components.SingletonComponent class *
-keep class * extends dagger.hilt.internal.GeneratedComponent
-keepclassmembers class * {
    @dagger.hilt.android.qualifiers.ApplicationContext <fields>;
}

# ===============================================================================
# Gson
# ===============================================================================
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontwarn sun.misc.**

# Keep Gson classes
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Fix for TypeToken generic issue
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keepclassmembers class * extends com.google.gson.reflect.TypeToken {
    <fields>;
    <methods>;
}

# Keep JsonConverter and prevent obfuscation of generic types
-keep class com.moksh.kontext.data.utils.JsonConverter { *; }
-keepclassmembers class com.moksh.kontext.data.utils.JsonConverter {
    *;
}

# Preserve generic signatures for Gson
-keepattributes Signature
-keep class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep your data classes for Gson serialization  
-keep class com.moksh.kontext.data.model.** { *; }
-keep class com.moksh.kontext.domain.model.** { *; }
-keep class com.moksh.kontext.domain.dto.** { *; }

# ===============================================================================
# Kotlin Serialization
# ===============================================================================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontwarn kotlinx.serialization.internal.ClassValueReferences
-keep,includedescriptorclasses class com.moksh.kontext.**$$serializer { *; }
-keepclassmembers class com.moksh.kontext.** {
    *** Companion;
}
-keepclasseswithmembers class com.moksh.kontext.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ===============================================================================
# OkHttp
# ===============================================================================
-keeppackagenames okhttp3.internal.publicsuffix.*
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ===============================================================================
# Retrofit
# ===============================================================================
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# ===============================================================================
# Kotlin Coroutines
# ===============================================================================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }
-keepclassmembers class kotlin.coroutines.SafeContinuation { volatile <fields>; }

# ===============================================================================
# Jetpack Compose Navigation
# ===============================================================================
-keep class androidx.navigation.** { *; }
-keep class * extends androidx.navigation.NavArgs
-keepclassmembers class * extends androidx.navigation.NavArgs {
    *;
}

# Keep Navigation component arguments
-keepnames class * extends android.os.Parcelable
-keepnames class * implements java.io.Serializable

# ===============================================================================
# Coil Image Loading
# ===============================================================================
-keep class coil.** { *; }
-keep interface coil.** { *; }
-keep class * implements coil.decode.Decoder
-keep class * implements coil.fetch.Fetcher
-keep class * implements coil.map.Mapper
-keep class * implements coil.key.Keyer

# ===============================================================================
# AndroidX and Jetpack Compose
# ===============================================================================
-keep class * extends androidx.lifecycle.ViewModel
-keep class * extends androidx.lifecycle.AndroidViewModel
-keep class androidx.compose.** { *; }
-keep class androidx.activity.compose.** { *; }
-keep class androidx.lifecycle.compose.** { *; }

# ===============================================================================
# Android Components
# ===============================================================================
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep BuildConfig
-keep class com.moksh.kontext.BuildConfig { *; }
-dontwarn com.moksh.kontext.BuildConfig

# ===============================================================================
# Security and Credentials
# ===============================================================================
-keep class androidx.credentials.** { *; }
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.gms.common.** { *; }

# Google Crypto Tink (fix for missing classes)
-dontwarn com.google.api.client.**
-dontwarn org.joda.time.**
-dontwarn com.google.crypto.tink.util.KeysDownloader
-keep class com.google.crypto.tink.** { *; }

# ===============================================================================
# Reflection
# ===============================================================================
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

# ===============================================================================
# Enum classes
# ===============================================================================
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ===============================================================================
# General Android
# ===============================================================================
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider