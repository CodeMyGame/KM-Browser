package com.kapilmalviya.km_browser;

import android.app.Dialog;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Kapil Malviya on 12/14/2015.
 */
public class ViewClient extends WebViewClient {


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      // obj.pb.setVisibility(view.VISIBLE);

        view.loadUrl(url);
        // return super.shouldOverrideUrlLoading(view, url);

        return true;
    }

}
