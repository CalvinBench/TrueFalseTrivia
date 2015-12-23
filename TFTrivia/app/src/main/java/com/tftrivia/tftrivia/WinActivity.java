package com.tftrivia.tftrivia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class WinActivity extends AppCompatActivity {

    MediaPlayer player;
    //String objectID;
    //String[] catResponse;
    String win = "";
    int trophy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        //objectID = intent.getStringExtra("objectID");
        //catResponse = intent.getStringArrayExtra("catResponse");
        win = intent.getStringExtra("win");
        trophy = intent.getIntExtra("trophy", 0);

        TextView text = (TextView) findViewById(R.id.congrats);
        text.setText(Html.fromHtml("<b>Congrats!</b>" + win));

        ImageView image = (ImageView) findViewById(R.id.winImage);
        switch (trophy) {
            case 1:
                image.setImageDrawable(getResources().getDrawable(R.drawable.trophy_1));
                break;
            case 2:
                image.setImageDrawable(getResources().getDrawable(R.drawable.trophy_2));
                break;
            case 3:
                image.setImageDrawable(getResources().getDrawable(R.drawable.trophy_3));
                break;
            case 4:
                image.setImageDrawable(getResources().getDrawable(R.drawable.trophy_4));
                break;
            default:
                break;
        }

        if (((MyApplication) getApplication()).getSound()) {
            player = new MediaPlayer();
            player = MediaPlayer.create(this, R.raw.gangstarr_clip);
            player.start();
        }

//        (findViewById(R.id.screen)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doLaunch();
//            }
//        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doLaunch();
            }
        }, 5000);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void doLaunch() {

        Intent triviaIntent = new Intent(getApplicationContext(), TriviaActivity.class);
        //triviaIntent.putExtra("objectID", objectID);
        //triviaIntent.putExtra("catResponse", catResponse);
        startActivity(triviaIntent);

    }

}
