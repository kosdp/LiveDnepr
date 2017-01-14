package com.dnepr.livednepr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invalidateOptionsMenu();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Set<String> cams = getCams();
        drawCams(cams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,0,1,"Reload").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE,1,0,"Refresh List").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                reload();
                break;
            case 1:
                refresh();
                reload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reload() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void refresh() {
        Set<String> cams = new HashSet<>();
        for(int i = 0; i< 1000; i++) {
            try {
                String cam = "c" + String.valueOf(i);
                //String loc = "http://live.dnepr.com:1935/live/c" + cam + ".stream/playlist.m3u8";
                String loc = "http://live.dnepr.com/static/" + cam + "/cam.jpg";
                //String loc = "http://live.dnepr.com/mjpeg/" + cam + "/video.mjpg";
                URL url = new URL(loc);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int code = conn.getResponseCode();
                if (code == 200) {
                    cams.add(cam);
                }
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("cams", cams);
        editor.apply();
    }

    private void drawCams(Set<String> cams) {
        LinearLayout sc = new LinearLayout(this);
        sc.setOrientation(LinearLayout.VERTICAL);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        for (String cam:cams) {
            ImageView iview = new ImageView(this);
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(new URL("http://live.dnepr.com/static/" + cam + "/cam.jpg").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            iview.setImageBitmap(bm);
            sc.addView(iview, width, width*384/512);
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(sc);
        setContentView(sv);
    }

    private Set<String> getCams() {
        String[] camsId = {"c54","c72","c74","c76","c92","c251","c414","c418","c419","c420","c422","c424","c425","c426","c710","c711","c716","c811"};
        //String[] videoCams = {"c54","c72","c710","c711"};
        Set<String> cams = new HashSet<>(Arrays.asList(camsId));

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        cams = preferences.getStringSet("cams", cams);


        return cams;
    }

}