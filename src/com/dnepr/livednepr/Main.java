package com.dnepr.livednepr;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by root on 14.02.2016.
 */
public class Main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //TextView view = new TextView(this);
        //view.setText("Hello");
        int[]camsId = {53,54,72,73,74,76,78,79,83,84,87,92,414,418,420,422,423,424,425};
        LinearLayout sc = new LinearLayout(this);
        sc.setOrientation(LinearLayout.VERTICAL);
        for (int camId:camsId) {
            ImageView iview = new ImageView(this);
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(new URL("http://live.dnepr.com/static/c" + camId + "/cam.jpg").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            iview.setImageBitmap(bm);
            sc.addView(iview, 1024,768);
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(sc);
        setContentView(sv);
    }

}