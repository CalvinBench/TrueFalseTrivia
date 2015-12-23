package com.tftrivia.tftrivia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.parse.SaveCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//design and some of the code for this project was inspired by Professor Emmet Witchel's parseDemo
public class MainActivity extends AppCompatActivity {

    boolean newGame;

    String username;

    String objectID;
    ArrayList<Integer> trophyCounts;
    ArrayList<Boolean> trophies;
    ArrayList<String> correctsArray;
    int trophyNum;

    protected void parseInit() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Add your initialization code here
        Parse.initialize(this, "NsZUKs5dpOjeq2quU2IooZsiriF3ltEktqeqs0Uy", "VpG82v3V3ZZMrBG6273pLZBsDSHhfdss2nwVlOKe");

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        //defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        newGame = false;

        Intent intent = getIntent();

        newGame = intent.getBooleanExtra("newGame", false);

        if (!newGame)
            parseInit();

        if (ParseUser.getCurrentUser() != null)
            ParseUser.getCurrentUser().logOut();


        System.out.println("This is the current user1: " + ParseUser.getCurrentUser());

//        final ProgressDialog dlg = new ProgressDialog(this);
//        dlg.setTitle("Please wait");
//        dlg.setMessage("Logging in");
//        dlg.show();

        if (ParseUser.getCurrentUser() == null) {

            Random random = new Random(System.nanoTime());
            username = ("" + random.nextInt(1000000000));

            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword("password");

            trophies = new ArrayList<>(18);
            for (int i = 0; i < 18; i++) {
                trophies.add(false);
            }

            trophyCounts = new ArrayList<>(18);
            for (int i = 0; i < 18; i++) {
                trophyCounts.add(0);
            }

            correctsArray = new ArrayList<String>();
            correctsArray.add("");
            trophyNum = -1;


            // Call the Parse signup method
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        // Show the error message
                        System.out.println("We haven't signed up and there's an error: " + e.getMessage());
                    } else {
                        System.out.println("We've signed up!");
                        System.out.println("This is our current user: " + ParseUser.getCurrentUser().getUsername());


                        final ParseObject records = new ParseObject("Records");
                        records.put("playerName", username);
                        records.put("trophies", trophies);
                        records.put("trophyCounts", trophyCounts);
                        records.put("trophyNum", trophyNum);
                        records.put("sound", true);
                        records.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Success!
                                    String objectId = records.getObjectId();
                                    objectID = objectId;
                                    ((MyApplication) getApplication()).setObjectID(objectID);
                                    System.out.println("This is OBJECT ID in MAIN1: " + objectID);

                                    final ParseObject corrects = new ParseObject("Corrects");
                                    corrects.put("playerID", objectID);
                                    corrects.put("corrects", correctsArray);
                                    corrects.saveInBackground();

                                } else {
                                    System.out.println("Save in background didn't quite work our: " + e.getMessage());
                                }
                            }
                        });

                    }
                }

            });


        } else {

            username = ParseUser.getCurrentUser().getUsername();

        }


        System.out.println("This is OBJECT ID in MAIN2: " + objectID);


        (findViewById(R.id.screen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLaunch();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doLaunch();
            }
        }, 4000);

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
        Intent trophyIntent = new Intent(getApplicationContext(), TrophyActivity.class);
        trophyIntent.putExtra("objectID", objectID);
        startActivity(trophyIntent);
    }

}