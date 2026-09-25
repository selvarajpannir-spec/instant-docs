# Keep JS interface methods
-keepclassmembers class com.instadocs.app.AppBridge {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep WebView related
-keepclassmembers class * extends android.webkit.WebViewClient {
    public *;
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public *;
}

# Compose / general
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }
