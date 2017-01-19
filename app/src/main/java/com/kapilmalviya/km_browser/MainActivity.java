package com.kapilmalviya.km_browser;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,TextToSpeech.OnInitListener {

    ProgressBar pb;
    WebView wB;
    EditText url;
    SwipeRefreshLayout sfl;
    FloatingActionButton fab,fabplus;
    Vibrator vb;
    Toolbar toolbar;
    LinearLayout urllayout,webviewLinearLayout;
    Button cancel;

   Animation animate,animateback;

    public static ImageView dp;
    ArrayList<String> result = new ArrayList<>();
    String speach="";
    String defaultBrowser = "";
    SharedPreferences pref;
    ShareLinkContent content;
    TextToSpeech tts;
    ContextMenu menu;

    public  static TextView loginID;
    public static List<Information> list;
    public static List<Information> listhistory;
    SharedPreferences shared;
    public  static TextView profilename;
boolean doubl = false;
    @Override
    public void onBackPressed() {
        if(wB.canGoBack()) {
            wB.goBack();
        }else{

            if(doubl){
                super.onBackPressed();
            }
            this.doubl = true;
            Toast.makeText(MainActivity.this, "Please click back again to exit", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubl = false;
                }
            }, 2000);
        }


    }
public void loadImageFromInteralMemory(String path){
    try{
        File f = new File(path,"profile.jpg");
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
        dp.setImageBitmap(b);
    }catch (Exception e){
        Toast.makeText(getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
    }
}
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);                               //to support both in kitkat and lollipop
        MultiDex.install(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                if(resultCode==RESULT_OK&&null!=data){
                    result.remove(0);
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    wB.loadUrl("http://www.google.co.in/search?q="+result.get(0)+"&oq="+result.get(0)+"aqs=mobile-qws-lite..");
                    Toast.makeText(MainActivity.this,result.get(0), Toast.LENGTH_LONG).show();
                    speach = "you are searching for ";
                    speak();
                }else{
                    try {
                        speach = "no data recognized please try again";
                        Toast.makeText(MainActivity.this, speach, Toast.LENGTH_LONG).show();
                       // result.remove(0);
                       // result.add(0, speach);

                      //  speak();
                    }catch(Exception r){
                        Toast.makeText(MainActivity.this, "ERROR:" + r, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    private void voiceToText(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.mic));
        try{
            startActivityForResult(i,100);
        }catch(Exception e){
            Toast.makeText(MainActivity.this, "Something went wrong!!!", Toast.LENGTH_LONG).show();
        }
    }
public void tabs(){

}
public void goaction(){
    try {

        if(url.getText().toString().length()!=0) {
            vb.vibrate(20);

            if (isNetworkAvailable()) {
                String address = url.getText().toString();
                StringBuilder sb = new StringBuilder(address);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (sb.charAt(0) == 'h' && sb.charAt(1) == 't' && sb.charAt(2) == 't' && sb.charAt(3) == 'p') {
                    wB.loadUrl(address);

                    imm.hideSoftInputFromWindow(url.getWindowToken(),0); //hide keyboard on click go


                } else {
                    wB.loadUrl("http://" + address);
                    imm.hideSoftInputFromWindow(url.getWindowToken(), 0);
                }

            } else {
                Toast.makeText(MainActivity.this, "ERROR:Please check your network connection!!!", Toast.LENGTH_LONG).show();
                wB.loadUrl("file:///android_asset/feedback.html");

            }
        }else{
            Toast.makeText(MainActivity.this, "URL not to be empty!!!", Toast.LENGTH_LONG).show();
        }
    } catch (Exception r) {
        Toast.makeText(MainActivity.this, "ERROR:" + r, Toast.LENGTH_LONG).show();
    }
}
    private void initialize(){
        //to get updated browser list
        profilename = (TextView) findViewById(R.id.profileName);
        loginID = (TextView) findViewById(R.id.loginid);


        pref = PreferenceManager.getDefaultSharedPreferences(this);

        CharSequence[] title = getBaseContext().getResources().getStringArray(R.array.pref_default_browser_list_titles);
        String mytitle = title[Integer.parseInt(pref.getString("browser_list","2"))].toString();
        defaultBrowser = mytitle;
        ///
        webviewLinearLayout = (LinearLayout) findViewById(R.id.webLinearLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        sfl = (SwipeRefreshLayout) findViewById(R.id.swipe);
        wB = (WebView) findViewById(R.id.webView);
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setMax(100);
        url = (EditText) findViewById(R.id.url);
        url.setHintTextColor(getResources().getColor(R.color.colorEdit));
        dp = (ImageView) findViewById(R.id.imageView);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        urllayout=(LinearLayout) findViewById(R.id.urllayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabplus = (FloatingActionButton) findViewById(R.id.fabadd);

    }
    private void onlistners(){


        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == R.id.search_go_btn || actionId == EditorInfo.IME_NULL) {
                    if (url.getText() != null) {
                        goaction();
                    } else {
                        Toast.makeText(MainActivity.this, "URL not to be empty!!!", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }

                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    goaction();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "ERROR:" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        fabplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(getBaseContext(),MainActivity.class));
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl.setRefreshing(true);
                if (isNetworkAvailable()) {
                    wB.reload();
                } else {
                    Toast.makeText(MainActivity.this, "ERROR:Please check your network connection!!!", Toast.LENGTH_LONG).show();
                }
                fetchTimelineAsync();
            }
        });
        sfl.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_orange_light, android.R.color.holo_green_light,
                android.R.color.holo_red_dark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_navigation);
            FacebookSdk.sdkInitialize(getApplicationContext());
            initialize();
             dpset();
            tabs();
           onlistners();
            webViewSettings();
            tts = new TextToSpeech(this,this);
            result.add(0, "Welcome in KM browser");
            content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();

            if (isNetworkAvailable()) {

            } else {
                Toast.makeText(MainActivity.this, "ERROR:Please check your network connection!!!", Toast.LENGTH_LONG).show();
                wB.loadUrl("file:///android_asset/feedback.html");

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }catch (Exception e){
            Toast.makeText(MainActivity.this,""+e, Toast.LENGTH_LONG).show();
        }
    }

    private void dpset(){
if(AccessToken.getCurrentAccessToken()!=null) {
    shared = getSharedPreferences("login_data",MODE_PRIVATE);
    if ((shared.getString("internalPath", null)) != null) {
       // Picasso.with(getBaseContext()).load(shared.getString("url", null)).transform(new CircleTransform()).into(dp);
        profilename.setText(shared.getString("name", null));
        loginID.setText(shared.getString("id", null));
        loadImageFromInteralMemory(shared.getString("internalPath",null));
    }

}


    }
    private void fetchTimelineAsync() {
    if(isNetworkAvailable()){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sfl.setRefreshing(false);
            }
        }, 5000);
    }
    else{
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sfl.setRefreshing(false);
            }
        }, 500);
    }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menuu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menuu, v, menuInfo);
        final WebView.HitTestResult result = wB.getHitTestResult();
        menu = menuu;
        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle()=="save image"){

                }
                return true;
            }
        };

            menu.setHeaderTitle("KM-Menu");
            menu.add(0,123,0,"save image").setOnMenuItemClickListener(handler);
            menu.add(0,122,0,"Call on this number").setOnMenuItemClickListener(handler);
            menu.add(0,121,0,"Email on this vie gmail").setOnMenuItemClickListener(handler);

    }



    private void webViewSettings() {


        wB.setWebViewClient(new ViewClient());  //to open new link in same web application
        wB.getSettings().setJavaScriptEnabled(true);  //some sites uses java_scripts like youtube.com
        wB.getSettings().setUseWideViewPort(true);
        wB.getSettings().setLoadWithOverviewMode(true);
        wB.getSettings().setDisplayZoomControls(true);
        wB.getSettings().setSupportZoom(true);
       // wB.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        wB.getSettings().setBuiltInZoomControls(true);
        wB.requestFocus();
        registerForContextMenu(wB);
        wB.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        wB.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wB.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wB.requestFocusFromTouch();
        wB.getSettings().setLoadsImagesAutomatically(true);
        wB.loadUrl("http://www." + defaultBrowser.toLowerCase() + ".com");
        wB.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100 && pb.getVisibility() == (ProgressBar.GONE)) {
                    pb.setVisibility(ProgressBar.VISIBLE);
                    pb.setProgress(newProgress);
                }
                if (newProgress == 100 && pb.getVisibility() == (ProgressBar.VISIBLE)) {
                    pb.setVisibility(ProgressBar.GONE);
                }

            }


        });


        wB.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    Toast.makeText(MainActivity.this, "Downloading....", Toast.LENGTH_LONG).show();
                    String file = getfilename(url);
                    Uri source = Uri.parse(url);
                    DownloadManager.Request req = new DownloadManager.Request(source);
                    req.setDescription(url);
                    req.setTitle(file);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        req.allowScanningByMediaScanner();
                    }
                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url);
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(req);


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "ERROR : " + e, Toast.LENGTH_LONG).show();
                }

            }
        });



    }
    private String getfilename(String downloadurl){
        try {
            String filename = "";
            int countslash = 0;

            int length = downloadurl.length();
            for (int i = 0; i < length; i++) {
                if (downloadurl.charAt(i) == '/') {
                    countslash++;
                }
            }
            int temp=0;
            int j=0;
           while(temp!=countslash){
               if(downloadurl.charAt(j)=='/'){
                   temp++;
               }
               j++;
           }
            StringBuilder sb = new StringBuilder(downloadurl);
            filename = sb.delete(0,j).toString();
            return filename;
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "ERROR : " + e, Toast.LENGTH_LONG).show();
        }
        return null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }


    public List<Information> getData(){
         list = new ArrayList<>();
        WebBackForwardList historylist = wB.copyBackForwardList();
        for(int i=0;i<historylist.getSize();i++){
            Information info = new Information();
            WebHistoryItem items = historylist.getItemAtIndex(i);
            String urls = items.getUrl();
            int icons =R.mipmap.ic_launcher;
            info.title =urls;
            info.itemId =icons;
            list.add(info);
        }
        return list;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){
            startActivity(new Intent(getBaseContext(),SettingsActivity.class));
        }
        if (id == R.id.stop) {
            wB.stopLoading();
        }
        if (id == R.id.newtab) {
            voiceToText();
        }
        if (id == R.id.search) {
            if(urllayout.getVisibility()==View.GONE) {
                try {
                   animateback = AnimationUtils.loadAnimation(this,R.anim.backedit);
                    animate = AnimationUtils.loadAnimation(this,R.anim.edittext);
                    urllayout.setVisibility(View.VISIBLE);
                    urllayout.setAnimation(animate);
                    fab.setVisibility(View.VISIBLE);
                    fab.setAnimation(animateback);
                    WebBackForwardList historylist = wB.copyBackForwardList();
                    WebHistoryItem items =null;
                    int size = historylist.getSize();
                    if(historylist!=null) {
                        items = historylist.getItemAtIndex(size - 1);
                    }
                    String urlss =null;
                    if(items!=null) {
                        urlss = items.getUrl();
                    }
                    if(urlss!=null) {
                        url.setText(urlss);
                        //cancel.setVisibility(View.VISIBLE);

                    }
                }catch(Exception e){
                    Toast.makeText(MainActivity.this,"Exception:"+e, Toast.LENGTH_SHORT).show();
                }
            }else {

                urllayout.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
              //  cancel.setVisibility(View.GONE);
            }

        }
        if (id == R.id.refresh) {
            if (isNetworkAvailable()) {

                wB.reload();
                Toast.makeText(MainActivity.this, "Refreshing....", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "ERROR:Please check your network connection!!!", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.history) {
            try{
                listhistory = getData();
                startActivity(new Intent(MainActivity.this, History.class));
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.search_next_btn) {
            if (isNetworkAvailable()) {
                if (wB.canGoForward()) {
                    wB.goForward();
                } else {
                    Toast.makeText(MainActivity.this, "Next page not available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "ERROR:Please check your network connection!!!", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.share) {
            try {
                share();
            }catch(Exception e){
                Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.about) {
            startActivity(new Intent(this, About.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public void share(){
        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String body = "Please download my web browser(KM browser) on Google play";
            share.putExtra(Intent.EXTRA_SUBJECT, "KM-Browser");
            share.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(share, "share via"));
        }catch(Exception e){
            Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {

          wB.clearHistory();
            Toast.makeText(MainActivity.this, "History cleared...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_catche) {
            wB.clearFormData();
            Toast.makeText(MainActivity.this, "Catche and form data cleared....", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_download) {
            try {
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_share) {
            try {
                share();
            }catch(Exception e){
                Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.login) {
            try {
                startActivity(new Intent(getBaseContext(), LoginActivityFb.class));
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.cancel){
            url.setText("");

        }
    }


    @Override
    protected void onDestroy() {
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        try{
        if(status==TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.US);
            if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(MainActivity.this, "This language is not supported!!!", Toast.LENGTH_SHORT).show();
            }else{
                speak();
            }
        }else{
            Toast.makeText(MainActivity.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
        }

    }catch(Exception e){
            Toast.makeText(MainActivity.this, "ERROR:"+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void speak() {

        tts.speak(speach+result.get(0),TextToSpeech.QUEUE_FLUSH,null);

    }


}

