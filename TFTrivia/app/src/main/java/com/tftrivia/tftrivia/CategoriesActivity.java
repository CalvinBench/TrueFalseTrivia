package com.tftrivia.tftrivia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    MediaPlayer player;
    //String objectID;
    //String[] catResponse = new String[2];

    ArrayList<View> foundViews = new ArrayList<>(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent = getIntent();
        //objectID = ((MyApplication) this.getApplication()).getObjectID();
        //objectID = intent.getStringExtra("objectID");
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


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
                Toast.makeText(getApplicationContext(), "No 'How To' just yet", Toast.LENGTH_LONG).show();
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
//                    menuItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            catResponse[0] = "*";
//                            Intent triviaIntent = new Intent(getApplicationContext(), TrophyActivity.class);
//                            triviaIntent.putExtra("catResponse", catResponse);
//                            triviaIntent.putExtra("countForTrophies", countForTrophies);
//                            startActivity(triviaIntent);
//                        }
//                    });
//
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



        (findViewById(R.id.random)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MyApplication) getApplication()).getSound()) {
                    player = new MediaPlayer();
                    player = MediaPlayer.create(getApplicationContext(), R.raw.random_clip);
                    player.start();
                }

                ((MyApplication) getApplication()).setCategory("*");
                Intent triviaIntent = new Intent(getApplicationContext(), TriviaActivity.class);
                //triviaIntent.putExtra("objectID", objectID);
                //triviaIntent.putExtra("catResponse", catResponse);
                startActivity(triviaIntent);
            }
        });

        (findViewById(R.id.culture)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MyApplication) getApplication()).getSound()) {
                    player = new MediaPlayer();
                    player = MediaPlayer.create(getApplicationContext(), R.raw.symphony_clip);
                    player.start();
                }

                ((MyApplication) getApplication()).setCategory("Culture");
                Intent triviaIntent = new Intent( getApplicationContext(), TriviaActivity.class);
                //triviaIntent.putExtra("objectID", objectID);
                //triviaIntent.putExtra("catResponse", catResponse);
                startActivity(triviaIntent);
            }
        });


        (findViewById(R.id.entertainment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MyApplication) getApplication()).getSound()) {
                    player = new MediaPlayer();
                    player = MediaPlayer.create(getApplicationContext(), R.raw.kermit_clip);
                    player.start();
                }

                ((MyApplication) getApplication()).setCategory("Entertainment");
                Intent triviaIntent = new Intent( getApplicationContext(), TriviaActivity.class);
                //triviaIntent.putExtra("objectID", objectID);
                //triviaIntent.putExtra("catResponse", catResponse);
                startActivity(triviaIntent);
            }
        });

        (findViewById(R.id.geography)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MyApplication) getApplication()).getSound()) {
                    player = new MediaPlayer();
                    player = MediaPlayer.create(getApplicationContext(), R.raw.airplane_clip);
                    player.start();
                }

                ((MyApplication) getApplication()).setCategory("Geography");
                Intent triviaIntent = new Intent( getApplicationContext(), TriviaActivity.class);
                //triviaIntent.putExtra("objectID", objectID);
                //triviaIntent.putExtra("catResponse", catResponse);
                startActivity(triviaIntent);
            }
        });

        (findViewById(R.id.history)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MyApplication) getApplication()).getSound()) {
                    player = new MediaPlayer();
                    player = MediaPlayer.create(getApplicationContext(), R.raw.indiana_jones_clip);
                    player.start();
                }

                ((MyApplication) getApplication()).setCategory("History");
                Intent triviaIntent = new Intent( getApplicationContext(), TriviaActivity.class);
                //triviaIntent.putExtra("objectID", objectID);
                //triviaIntent.putExtra("catResponse", catResponse);
                startActivity(triviaIntent);
            }
        });

        (findViewById(R.id.plantsAnimals)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MyApplication) getApplication()).getSound()) {
                    player = new MediaPlayer();
                    player = MediaPlayer.create(getApplicationContext(), R.raw.animals_clip);
                    player.start();
                }

                ((MyApplication) getApplication()).setCategory("PlantsAnimals");
                Intent triviaIntent = new Intent( getApplicationContext(), TriviaActivity.class);
                //triviaIntent.putExtra("objectID", objectID);
                //triviaIntent.putExtra("catResponse", catResponse);
                startActivity(triviaIntent);
            }
        });

        (findViewById(R.id.science)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MyApplication) getApplication()).getSound()) {
                    player = new MediaPlayer();
                    player = MediaPlayer.create(getApplicationContext(), R.raw.science_clip);
                    player.start();
                }

                ((MyApplication) getApplication()).setCategory("Science");
                Intent triviaIntent = new Intent( getApplicationContext(), TriviaActivity.class);
                //triviaIntent.putExtra("objectID", objectID);
                //triviaIntent.putExtra("catResponse", catResponse);
                startActivity(triviaIntent);
            }
        });

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


//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_category) {
//            Intent categoriesActivityIntent = new Intent(getApplicationContext(),CategoriesActivity.class);
//            startActivity(categoriesActivityIntent);
//            return true;
//        } else if (id == R.id.nav_trophy) {
//            Intent trophyIntent = new Intent( this, TrophyActivity.class);
//            trophyIntent.putExtra("catResponse", catResponse);
//            startActivity(trophyIntent);
//            return true;
//        } else if (id == R.id.nav_manage) {
//            Intent LoginIntent = new Intent( this, LoginActivity.class);
//            final int result = 1;
//            startActivityForResult(LoginIntent, result);
//            return true;
//        } else if (id == R.id.correct) {
//
//        } else if (id == R.id.incorrect) {
//
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    public void clickCategory() {
        Intent categoryIntent = new Intent( this, CategoriesActivity.class);
        //categoryIntent.putExtra("objectID", objectID);
        //categoryIntent.putExtra("catResponse", catResponse);
        startActivity(categoryIntent);
    }

    public void clickTrophy() {
        Intent trophyIntent = new Intent( this, TrophyActivity.class);
        //trophyIntent.putExtra("objectID", objectID);
        //trophyIntent.putExtra("catResponse", catResponse);
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
