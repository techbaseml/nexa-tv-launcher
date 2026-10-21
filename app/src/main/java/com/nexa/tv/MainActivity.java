package com.nexa.tv;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity {
  private WebView web;
  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    getWindow().getDecorView().setSystemUiVisibility(5894 | 1024 | 512);
    web = new WebView(this);
    web.getSettings().setJavaScriptEnabled(true);
    web.getSettings().setAllowFileAccess(true);
    web.addJavascriptInterface(new Bridge(), "Nexa");
    web.setWebViewClient(new WebViewClient());
    setContentView(web);
    web.loadUrl("file:///android_asset/index.html");
  }
  public class Bridge {
    @JavascriptInterface public String apps() {
      JSONArray a = new JSONArray();
      try {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> results = new ArrayList<>(pm.queryIntentActivities(intent, 0));
        Intent normal = new Intent(Intent.ACTION_MAIN);
        normal.addCategory(Intent.CATEGORY_LAUNCHER);
        results.addAll(pm.queryIntentActivities(normal, 0));
        Collections.sort(results, Comparator.comparing(r -> r.loadLabel(pm).toString().toLowerCase()));
        java.util.HashSet<String> seen = new java.util.HashSet<>();
        for (ResolveInfo r : results) {
          String pkg = r.activityInfo.packageName;
          if (pkg.equals(getPackageName()) || !seen.add(pkg)) continue;
          JSONObject o = new JSONObject();
          o.put("name", r.loadLabel(pm).toString()); o.put("pkg", pkg); a.put(o);
        }
      } catch(Exception ignored) {}
      return a.toString();
    }
    @JavascriptInterface public void launch(String pkg) {
      runOnUiThread(() -> {
        try {
          PackageManager pm = getPackageManager();
          Intent i = pm.getLeanbackLaunchIntentForPackage(pkg);
          if (i == null) i = pm.getLaunchIntentForPackage(pkg);
          if (i != null) startActivity(i);
        } catch(Exception ignored) {}
      });
    }
  }
  @Override public void onBackPressed() { if (web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
