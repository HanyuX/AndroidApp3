package com.example.xuehanyu.stressmeter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity{
    private Fragment fragment;
    private Vibrator vibrator;       //vibrator
    private MediaPlayer mediaPlayer; //sound
    private static final int EXIT_APPLICATION = 0x0001;    //mark for finishing the application

    /*
     * Called when the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        //set pattern of vibrator
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {1000, 500, 1000, 500};
        vibrator.vibrate(pattern, 2);

        //set sound and start
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }catch(Exception exc){
            exc.printStackTrace();
        }

        //set alarm
        PSMScheduler vibratorScheduler = new PSMScheduler();
        vibratorScheduler.setSchedule(this);

        fragment = new ImageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }

    /*
     * Called when the activity is created
     */
    protected void onResume() {
        int flag = getIntent().getIntExtra("flag", 0);
        if(flag == EXIT_APPLICATION) {
            stopMedia();
            finish();
        }
        super.onResume();

    }

    /*
     * Called when the back button is clicked
     */
    @Override
    public void onBackPressed() {
        stopMedia();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
     * Set up the drawer menu and clicker
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    /*
     *  Called when item in navigation bar is selected
     */
    public void selectDrawerItem(MenuItem menuItem) {
        stopMedia();
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_result:
                fragmentClass = chartFragment.class;
                break;
            case R.id.nav_stress:
                fragmentClass = ImageFragment.class;
                break;
            default:
                fragmentClass = ImageFragment.class;
            break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        onBackPressed();
    }

    /*
     * When the activity stops, vibrate and sound stops.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        stopMedia();
    }

    /*
     * Called when the media is stopped.
     */
    public void stopMedia(){
        vibrator.cancel();
        mediaPlayer.stop();
    }
}
