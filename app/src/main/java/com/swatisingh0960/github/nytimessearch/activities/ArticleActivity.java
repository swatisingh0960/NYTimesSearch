package com.swatisingh0960.github.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.swatisingh0960.github.nytimessearch.R;
import com.swatisingh0960.github.nytimessearch.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Article article = Parcels.unwrap(getIntent().getParcelableExtra("article"));

        getSupportActionBar().setTitle(article.getHeadline());

        final String url = getIntent().getStringExtra("url");
        WebView webView = (WebView) findViewById(R.id.wvArticle);

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
                }
        });
        webView.loadUrl(article.getWebUrl());
    }

    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu resource file
        getMenuInflater().inflate(R.menu.menu_article, menu);
        //Locate MenuItem with ShareAction Provider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        //get references to Webview
        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
        //pass in the URL currently being used by the Webview
        shareIntent.putExtra(Intent.EXTRA_TEXT,wvArticle.getUrl());

        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

}
