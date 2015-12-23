package com.tftrivia.tftrivia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.view.BezelImageView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TrophyActivity extends AppCompatActivity {
        //implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<View> foundViews;
    ArrayList<Boolean> trophyBool;
    ArrayList<Integer> trophyInt;
    String objectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //toolbar.inflateMenu(R.menu.main);

        //setTheme(android.R.style.Theme_Holo);

        // Drawer code and framework taken from Material Drawer library https://github.com/mikepenz/MaterialDrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        new DrawerBuilder().withActivity(this).build();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Categories").withDescription("Choose from 6!").withIcon(getResources().getDrawable(R.drawable.categories));
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Trophies").withDescription("View the trophies you've earned").withIcon(getResources().getDrawable(R.drawable.trophies));
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("How To").withDescription("Get the details on gameplay").withIcon(getResources().getDrawable( R.drawable.how_to));
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("New Game").withDescription("Your current game will be lost").withIcon(getResources().getDrawable( R.drawable.new_game));
        DividerDrawerItem item5 = new DividerDrawerItem();
        SwitchDrawerItem item6 = new SwitchDrawerItem().withName("Sound").withIcon(getResources().getDrawable(R.drawable.trophy_soul)).withChecked(((MyApplication) this.getApplication()).getSound()).withOnCheckedChangeListener(onCheckedChangeListener);



        //create the drawer and remember the `Drawer` result object
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(new ProfileDrawerItem().withIcon(getResources().getDrawable(R.drawable.ic_launcher)))
                //.setImageOrPlaceholder(new BezelImageView(this).setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher)), new ImageHolder(getResources().getDrawable( R.drawable.ic_launcher)))
                .withOnlyMainProfileImageVisible(false)
                .withHeaderBackground(R.drawable.black_paper)
                .build();

        //Now create your drawer and pass the AccountHeader.Result
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.paper_texture))
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6
                )
                .build();



        item1.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                clickCategory();
                return true;
            }
        });

        item2.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                clickTrophy();
                return true;
            }
        });

        item3.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Toast.makeText(getApplicationContext(), "No 'How To' just yet" , Toast.LENGTH_LONG).show();
                return true;
            }
        });

        item4.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                clickNewGame();
                return true;
            }
        });

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        //Intent intent = getIntent();
        objectID = ((MyApplication) this.getApplication()).getObjectID();
        System.out.println("THis is objectID in Trophy " + objectID);

        //        final Drawable myIcon = getResources().getDrawable( R.drawable.login );
//
//        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        final Menu navMenu = navigationView.getMenu();
//        // Install an OnGlobalLayoutListener and wait for the NavigationMenu to fully initialize
//        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // Remember to remove the installed OnGlobalLayoutListener
//                navigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                // Loop through and find each MenuItem View
//                for (int i = 0, length = 6; i < length; i++) {
//                    final String id = "menuItem" + (i + 1);
//                    final MenuItem item = navMenu.findItem(getResources().getIdentifier(id, "id", getPackageName()));
//                    navigationView.findViewsWithText(foundViews, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
//                }
//                // Loop through each MenuItem View and apply your custom Typeface
//                for (final View menuItem : foundViews) {
//                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds((TextView) menuItem, myIcon, null, null, null);
//                    System.out.println("VIEWS: " + menuItem);
//                }
//            }
//        });



//        Drawable myIcon = getResources().getDrawable( R.drawable.login );
//
//        // this will find all the textviews with the given text and put them in the arraylist - foundViews
//        navigationView.findViewsWithText(foundViews, "Categories", View.FIND_VIEWS_WITH_TEXT);
//
//        System.out.println("Found views: " + foundViews);
//
//        //then call set
//        //TextViewCompat.setCompoundDrawablesRelative((TextView) foundViews.get(0), myIcon, null, null, null);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        ParseQuery<ParseObject> query =ParseQuery.getQuery("Records");

        System.out.println("We're in RECORDS");
        System.out.println("This is our OBJECT ID: " + objectID);

        query.getInBackground(objectID, new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject object, ParseException e) {

                if (e == null) {
                    System.out.println("Have object W/O ERROR");

                    trophyInt = (ArrayList<Integer>) object.get("trophyCounts");
                    trophyBool = (ArrayList<Boolean>) object.get("trophies");

                    System.out.println("This is what's in trophies: " + trophyInt);
                    System.out.println("This is what's in trophies: " + trophyBool);


                    getTrophies(trophyBool);


                } else {
                    System.out.println("There's an ERROR");
                    Log.d("Brand", "Error: " + e.getMessage());
                }
            }
        });

    }

    private void startGame() {
        Intent categoriesActivityIntent = new Intent(getApplicationContext(),CategoriesActivity.class);
        categoriesActivityIntent.putExtra("objectID", objectID);
        startActivity(categoriesActivityIntent);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.content_test) {
//            Intent testActivityIntent = new Intent( this, TestActivity.class);
//            final int result = 1;
//            startActivityForResult(testActivityIntent, result);
//            return true;
//        } else if (id == R.id.parse_logout) {
//            if( ParseUser.getCurrentUser() != null ) {
//                ParseUser.getCurrentUser().logOut();
//            }
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void getTrophies(ArrayList<Boolean> trophyBool) {

        if (trophyBool.get(0)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_1 );
            ImageView image = (ImageView) findViewById(R.id.trophy_1);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(1)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_2 );
            ImageView image = (ImageView) findViewById(R.id.trophy_2);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(2)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_3 );
            ImageView image = (ImageView) findViewById(R.id.trophy_3);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(3)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_4 );
            ImageView image = (ImageView) findViewById(R.id.trophy_4);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(4)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_5 );
            ImageView image = (ImageView) findViewById(R.id.trophy_5);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(5)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_6 );
            ImageView image = (ImageView) findViewById(R.id.trophy_6);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(6)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_7 );
            ImageView image = (ImageView) findViewById(R.id.trophy_7);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(7)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_8 );
            ImageView image = (ImageView) findViewById(R.id.trophy_8);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(8)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_1 );
            ImageView image = (ImageView) findViewById(R.id.trophy_1);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(9)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_1 );
            ImageView image = (ImageView) findViewById(R.id.trophy_1);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(10)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_1 );
            ImageView image = (ImageView) findViewById(R.id.trophy_1);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(11)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_1 );
            ImageView image = (ImageView) findViewById(R.id.trophy_1);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(12)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_1 );
            ImageView image = (ImageView) findViewById(R.id.trophy_1);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(13)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_1 );
            ImageView image = (ImageView) findViewById(R.id.trophy_1);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(14)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_heart );
            ImageView image = (ImageView) findViewById(R.id.trophy_heart);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(15)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_soul );
            ImageView image = (ImageView) findViewById(R.id.trophy_soul);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(16)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_mind );
            ImageView image = (ImageView) findViewById(R.id.trophy_mind);
            image.setImageDrawable(myIcon);
        }

        if (trophyBool.get(17)) {
            Drawable myIcon = getResources().getDrawable( R.drawable.trophy_meta );
            ImageView image = (ImageView) findViewById(R.id.trophy_meta);
            image.setImageDrawable(myIcon);
        }


    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_category) {
//            Intent categoriesActivityIntent = new Intent(getApplicationContext(),CategoriesActivity.class);
//            categoriesActivityIntent.putExtra("objectID", objectID);
//            startActivity(categoriesActivityIntent);
//            return true;
//        } else if (id == R.id.nav_trophy) {
//            Intent trophyIntent = new Intent( this, TrophyActivity.class);
//            trophyIntent.putExtra("objectID", objectID);
//            startActivity(trophyIntent);
//            return true;
//        } else if (id == R.id.nav_manage) {
//            Intent LoginIntent = new Intent( this, LoginActivity.class);
//            LoginIntent.putExtra("objectID", objectID);
//            final int result = 1;
//            startActivityForResult(LoginIntent, result);
//            return true;
//        } else if (id == R.id.correct) {
//
//        } else if (id == R.id.incorrect) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        drawer.closeDrawer(GravityCompat.START);
//
//        return true;
//    }

    public void clickCategory() {
        Intent categoryIntent = new Intent( this, CategoriesActivity.class);
        //categoryIntent.putExtra("objectID", objectID);
        startActivity(categoryIntent);
    }

    public void clickTrophy() {
        Intent trophyIntent = new Intent( this, TrophyActivity.class);
        //trophyIntent.putExtra("objectID", objectID);
        startActivity(trophyIntent);
    }

    public void clickNewGame() {
        Intent newGameIntent = new Intent( this, MainActivity.class);
        newGameIntent.putExtra("newGame", true);
        startActivity(newGameIntent);
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
                ((MyApplication) getApplication()).setSound(!((MyApplication) getApplication()).getSound());
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

}