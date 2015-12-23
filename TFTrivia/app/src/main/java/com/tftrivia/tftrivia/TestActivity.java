package com.tftrivia.tftrivia;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class TestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected ImageView theImageView;
    protected View myRootView;

    Button testParseBut;
    TextView main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_test);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        View v = myRootView;

        getImage();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        //parseInit();
//        testParseBut = (Button) findViewById(R.id.testParse);
//        testParseBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                testParse();
//            }
//        });



        main = (TextView) findViewById(R.id.mainMessage);
        //updateMainMessage();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        //updateMainMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void testParse() {
        ParseObject note = new ParseObject("Notes");
        note.put("number", 42);
        note.put("text", "Call Luke Cage.");
        //note.put("content", "This note is private!");
        //note.setACL(new ParseACL(ParseUser.getCurrentUser()));
        note.saveInBackground();
        //updateMainMessage();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //updateMainMessage();
            return true;
        } else if (id == R.id.content_test) {
            Intent dynamicListDemoIntent = new Intent( this, TestActivity.class);
            final int result = 1;
            startActivityForResult(dynamicListDemoIntent, result);
            return true;
        } else if (id == R.id.parse_logout) {
            if( ParseUser.getCurrentUser() != null ) {
                ParseUser.getCurrentUser().logOut();
            }
            //updateMainMessage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.correct) {

        } else if (id == R.id.incorrect) {

        }
        //updateMainMessage();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getImageByNameCallback(String name, List<ParseObject> image) {
        Log.d("main", "got an image for name query " + name);

        final ImageView IV = this.theImageView;

        //grab the first one (if there are even multiple) and display it on screen
        ParseObject po = image.get(0);
        ParseFile theImage = (ParseFile)po.get("image");
        theImage.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
                IV.setBackground(image);
            }
        });

    }

    public void getImage() {
        // Locate the class table named "Trivias" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Trivias");

        // Locate the objectId from the class
        query.getInBackground("Vy5n5XtKaP",
                new GetCallback<ParseObject>()
                {

                    public void done(ParseObject object,
                                     ParseException e) {
                        // TODO Auto-generated method stub

                        // Locate the column named "image" and set
                        // the string
                        ParseFile fileObject = (ParseFile) object
                                .get("image");
                        fileObject
                                .getDataInBackground(new GetDataCallback() {

                                    public void done(byte[] data,
                                                     ParseException e) {
                                        if (e == null) {
                                            Log.d("test",
                                                    "We've got data in data.");
                                            // Decode the Byte[] into
                                            // Bitmap
                                            Bitmap bmp = BitmapFactory
                                                    .decodeByteArray(
                                                            data, 0,
                                                            data.length);

                                            // Get the ImageView from
                                            // main.xml
                                            ImageView image = (ImageView) findViewById(R.id.graphic);

                                            // Set the Bitmap into the
                                            // ImageView
                                            image.setImageBitmap(bmp);

                                        } else {
                                            Log.d("test",
                                                    "There was a problem downloading the data.");
                                        }
                                    }
                                });
                    }
                });
    }

}
