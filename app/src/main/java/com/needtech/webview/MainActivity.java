package com.needtech.webview;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int errorCode = -546;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);
        TextView textView = findViewById(R.id.textView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                textView.setVisibility(View.GONE);
                textView.setText("onPageFinished");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                textView.setVisibility(View.VISIBLE);
                textView.setText("onPageStarted");
                errorCode = -546;

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                textView.setVisibility(View.VISIBLE);
                errorCode = error.getErrorCode();

                // Build the error message
                String errorMessage = "Error: " + errorCode + "\n" + error.getDescription();
                textView.setText(errorMessage);

                // Show an AlertDialog with the error message and a reload button
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Error loading page")
                        .setMessage(errorMessage)
                        .setPositiveButton("Reload", (dialog, which) -> {
                            // Reload the WebView
                            webView.reload();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Dismiss the dialog
                            dialog.dismiss();
                        })
                        .show();

            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100 && errorCode != 546)
                    textView.setVisibility(View.VISIBLE);
                else textView.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(getString(R.string.url));
    }
}