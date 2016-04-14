package com.example.xuehanyu.stressmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

public class ShowImageActivity extends AppCompatActivity {

    private static final String TAG = "image_uri";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Intent intent = getIntent();
        int image = intent.getIntExtra(TAG, 0);
        ImageView imageView = (ImageView) findViewById(R.id.showimage);
        imageView.setImageResource(image);
    }

    public void onEntryCancelClicked(View view) {
        finish();
    }

    public void onEntrySubmitClicked(View view) {
        Random random = new Random();
        int num = random.nextInt(11) + 1;
        writeCSV("stress_timestamp.csv", System.currentTimeMillis(), num);
        finish();
    }

    public void writeCSV(String filename, long time, int stress) {
        try {
            OutputStream fout = openFileOutput(filename, MODE_APPEND);
            OutputStreamWriter bw = new OutputStreamWriter(fout);
            bw.write(time + "," + stress + "\r\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
