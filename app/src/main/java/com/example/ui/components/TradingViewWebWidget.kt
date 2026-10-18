package com.example.ui.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TradingViewWebWidget(pair: String) {
    val symbol = when (pair) {
        "EURUSD" -> "FX:EURUSD"
        "GBPUSD" -> "FX:GBPUSD"
        "XAUUSD" -> "OANDA:XAUUSD"
        "GBPJPY" -> "FX:GBPJPY"
        else -> "FX:EURUSD"
    }

    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <style>
                body { margin: 0; padding: 0; background-color: #05080C; overflow: hidden; }
                #tv_chart_container { width: 100vw; height: 100vh; }
            </style>
            <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
        </head>
        <body>
            <div id="tv_chart_container"></div>
            <script type="text/javascript">
                new TradingView.widget({
                    "autosize": true,
                    "symbol": "$symbol",
                    "interval": "15",
                    "timezone": "Etc/UTC",
                    "theme": "dark",
                    "style": "1",
                    "locale": "en",
                    "toolbar_bg": "#05080C",
                    "enable_publishing": false,
                    "hide_top_toolbar": false,
                    "hide_legend": false,
                    "save_image": false,
                    "container_id": "tv_chart_container",
                    "backgroundColor": "#05080C",
                    "gridColor": "rgba(255, 255, 255, 0.05)"
                });
            </script>
        </body>
        </html>
    """.trimIndent()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF05080C))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    webViewClient = WebViewClient()
                    setBackgroundColor(android.graphics.Color.parseColor("#05080C"))
                    loadDataWithBaseURL("https://www.tradingview.com", htmlContent, "text/html", "UTF-8", null)
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL("https://www.tradingview.com", htmlContent, "text/html", "UTF-8", null)
            }
        )
    }
}
