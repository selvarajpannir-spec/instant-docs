package com.instadocs.app

import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.util.Base64
import java.io.File
import java.io.FileOutputStream

class AppBridge(
    private val activity: MainActivity,
    private val webView: WebView
) {

    @JavascriptInterface
    fun exportPdf(fileName: String) {
        // Called from JS when user taps Download / Share
        activity.runOnUiThread {
            activity.exportPdf(fileName.ifBlank { "document.pdf" })
        }
    }

    @JavascriptInterface
    fun shareGeneratedPdf(base64Pdf: String, fileName: String) {
        // Alternative path: if JS ever generates PDF bytes itself
        activity.runOnUiThread {
            try {
                val pureBase64 = base64Pdf.substringAfter(",")
                val bytes = Base64.decode(pureBase64, Base64.DEFAULT)
                val cacheDir = activity.cacheDir
                val file = File(cacheDir, fileName.ifBlank { "document.pdf" })
                FileOutputStream(file).use { it.write(bytes) }
                activity.sharePdf(file.absolutePath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JavascriptInterface
    fun onBackResult(shouldExit: Boolean) {
        if (shouldExit) {
            activity.runOnUiThread {
                activity.finish()
            }
        }
    }
}
