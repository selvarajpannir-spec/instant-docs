package com.instadocs.app

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : ComponentActivity() {

    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle back button via JS
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                webView?.evaluateJavascript("window.appHandleBack && window.appHandleBack();", null)
            }
        })

        setContent {
            WebViewScreen { wv ->
                webView = wv
                setupWebView(wv)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(webView: WebView) {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            builtInZoomControls = false
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        // JS Bridge
        webView.addJavascriptInterface(AppBridge(this, webView), "AndroidBridge")

        // Load the app
        webView.loadUrl("file:///android_asset/index.html")
    }

    fun exportPdf(fileName: String) {
        val wv = webView ?: return

        val printAdapter = wv.createPrintDocumentAdapter(fileName)
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager

        // We drive the adapter manually to get a file without the system dialog
        val jobName = "InstaDocs_$fileName"
        val attributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setResolution(PrintAttributes.Resolution("pdf", "pdf", 300, 300))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()

        // For simplicity and reliability we still use PrintManager but with a custom callback path
        // A more advanced version can write the PDF bytes directly. For V1 we use share via FileProvider after generation.
        printManager.print(jobName, printAdapter, attributes)
    }

    fun sharePdf(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) return

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share document"))
    }
}

@Composable
fun WebViewScreen(onWebViewCreated: (WebView) -> Unit) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).also { onWebViewCreated(it) }
        }
    )
}
