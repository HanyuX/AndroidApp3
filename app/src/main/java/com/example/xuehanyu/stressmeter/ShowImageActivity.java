package com.example.xuehanyu.stressmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ShowImageActivity extends AppCompatActivity {

    private static final String TAG = "image_uri";         //mark for the image
    private static final String TAG1 = "position";         //mark for the
    private static final int EXIT_APPLICATION = 0x0001;    //mark for finishing the application

    //score associated with the position
    private int []indexToStress = {6, 8, 14, 16, 5, 7, 13, 15, 2, 4, 10, 12, 1, 3, 9, 11};

    //the index for the image sets
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        //received the data from the MainActivity
        Intent intent = getIntent();
        int image = intent.getIntExtra(TAG, 0);
        index = intent.getIntExtra(TAG1, 0);
        ImageView imageView = (ImageView) findViewById(R.id.showimage);
        imageView.setImageResource(image);
    }

    /*
     * Called when the cancel button is clicked
     */

    public void onEntryCancelClicked(View view) {
        finish();
    }

    /*
     * Called when the submit button is clicked
     */

    public void onEntrySubmitClicked(View view) {
        int stress = indexToStress[index];
        writeCSV("stress_timestamp.csv", System.currentTimeMillis(), stress);
        Intent mIntent = new Intent();
        mIntent.setClass(this, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.putExtra("flag", EXIT_APPLICATION);
        startActivity(mIntent);
    }

    /*
     * Write the time and the stress value to the csv file
     */
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
