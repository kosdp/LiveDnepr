package com.dnepr.livednepr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int[] camsId = getCams();
        drawCams(camsId);
    }

    private void drawCams(int[] camsId) {
        LinearLayout sc = new LinearLayout(this);
        sc.setOrientation(LinearLayout.VERTICAL);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        for (int camId:camsId) {
            ImageView iview = new ImageView(this);
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(new URL("http://live.dnepr.com/static/c" + camId + "/cam.jpg").openStream());
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

    private int[] getCams() {
        int[] camsId = {54,72,74,76,92,251,414,418,419,420,422,423,424,425,427,710,711,716,811};

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int storedPreference = preferences.getInt("storedInt", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("storedInt", storedPreference); // value to store
        editor.commit();

        return  camsId;
    }

}