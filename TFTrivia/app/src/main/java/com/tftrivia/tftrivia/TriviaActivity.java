package com.tftrivia.tftrivia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TriviaActivity extends AppCompatActivity {

    RelativeLayout lowestLayout;

    int trophyNum = -1;

    String objectID;
    String triviaName;
    Random rand;
    int value;
    String response;
    String category;

    //[0] == 3 nonconsecutive; [1] == 5 consecutive; [2] == 10 consecutive
    // trophyCounts
    ArrayList<Integer> countForTrophies;
    ArrayList<Boolean> boolForTrophies;
    ArrayList<String> corrects;

    Boolean answerBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        corrects = new ArrayList<String>();
        getCorrects();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        objectID = ((MyApplication) this.getApplication()).getObjectID();
        category = ((MyApplication) this.getApplication()).getCategory();

        rand = new Random();

        getImage();

        SwipeDetector swipeDetector = new SwipeDetector(this);
        lowestLayout = (RelativeLayout)this.findViewById(R.id.trivia);
        lowestLayout.setOnTouchListener(swipeDetector);

        (findViewById(R.id.truth)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTrue();
            }
        });
        (findViewById(R.id.falsehood)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFalse();
            }
        });


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

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

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
//            categoriesActivityIntent.putExtra("objectID", objectID);
//            categoriesActivityIntent.putExtra("catResponse", catResponse);
//            categoriesActivityIntent.putExtra("countForTrophies", countForTrophies);
//            startActivity(categoriesActivityIntent);
//            return true;
//        } else if (id == R.id.nav_trophy) {
//            Intent trophyIntent = new Intent( this, TrophyActivity.class);
//            trophyIntent.putExtra("objectID", objectID);
//            trophyIntent.putExtra("catResponse", catResponse);
//            trophyIntent.putExtra("countForTrophies", countForTrophies);
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
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    public void getCorrects() {

        final ProgressDialog dlg = new ProgressDialog(TriviaActivity.this);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Corrects");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    System.out.println("We're in DONE");
                    Log.d("Brand", "Retrieved " + objects + " Brands");

                } else {
                    System.out.println("We're in DONE2");
                    Log.d("Brand", "Error: " + e.getMessage());
                }

                if (objects == null || objects.size() == 0) {
                    System.out.println("We're in DONE3");
                    return;
                }

                ParseObject po = objects.get(0);

                corrects = (ArrayList<String>) po.get("corrects");

                dlg.dismiss();

            }
        });

    }


    public void getImage() {


        ParseQuery<ParseObject> trivia = new ParseQuery<ParseObject>(
                "Trivias");


        if (category.charAt(0) != '*')
            trivia.whereEqualTo("category", category);

        trivia.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    System.out.println("We're in DONE");
                    Log.d("Brand", "Retrieved " + objects + " Brands");

                } else {
                    System.out.println("We're in DONE2");
                    Log.d("Brand", "Error: " + e.getMessage());
                }

                if (objects == null || objects.size() == 0) {
                    System.out.println("We're in DONE3");
                    return;
                }

                System.out.println("THis is CORRECTS in TRIVIA: " + corrects);

                ParseObject po;

                do {

                    po = objects.get(rand.nextInt(objects.size()));
                    triviaName = (String) po.get("name");

                } while (corrects.contains(triviaName));


                System.out.println("This is the TRIVIANAMEafter " + triviaName);


                // Locate the column named "image" and set
                // the string
                ParseFile fileObject = (ParseFile) po
                        .get("image");

                String statement = (String) po.get("triviaStatement");
                response = (String) po.get("response");
                answerBool = po.getBoolean("answer");

//              System.out.println("THIS IS RESPONSE: " + response);

                TextView text = (TextView) findViewById(R.id.triviaText);
                text.setText(Html.fromHtml(statement));

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
                                    ImageView image = (ImageView) findViewById(R.id.triviaImage);

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

    public void updateTrophyCount(boolean bool) {

        final boolean boolz = bool;


        final ProgressDialog dlg = new ProgressDialog(TriviaActivity.this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Records");

        // Retrieve the object by id
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject records, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    countForTrophies = (ArrayList<Integer>) records.get("trophyCounts");
                    boolForTrophies = (ArrayList<Boolean>) records.get("trophies");
                    System.out.println("THis is trophyCountsBEFORE " + countForTrophies);

                    if (boolz) {

                        for (int i = 0; i < countForTrophies.size(); i++) {
                            value = countForTrophies.get(i);
                            value++;
                            countForTrophies.set(i, value);
                        }

                        // TURN ON AFTER PLAYTESTING
//                    } else {
//
//                        for (int i = 2; i < countForTrophies.size(); i++) {
//                            countForTrophies.set(i, 0);
//                        }

                    }

                    records.put("trophyCounts", countForTrophies);
                    records.saveInBackground();

                    System.out.println("THis is trophyCountsAFTER " + countForTrophies);

                    if (countForTrophies != null && countForTrophies.get(0) == 1 && !boolForTrophies.get(0)) {
                        System.out.println("YOU'VE WON A TROPHY");
                        records.put("trophyNum", 0);
                        updateTrophy(0);
                    }

                    if (countForTrophies != null && countForTrophies.get(1) == 3 && !boolForTrophies.get(1)) {
                        System.out.println("YOU'VE WON A TROPHY");
                        records.put("trophyNum", 1);
                        updateTrophy(1);
                    }

                    if (countForTrophies != null && countForTrophies.get(2) == 5 && !boolForTrophies.get(2)) {
                        System.out.println("YOU'VE WON A TROPHY");
                        records.put("trophyNum", 2);
                        updateTrophy(2);
                    }

                    if (countForTrophies != null && countForTrophies.get(3) == 10 && !boolForTrophies.get(3)) {
                        System.out.println("YOU'VE WON A TROPHY");
                        records.put("trophyNum", 3);
                        updateTrophy(3);
                    }

                }
                dlg.dismiss();

            }
        });
    }


    public void updateTrophy(final int index) {

        trophyNum = index;


        System.out.println("THis is trophyNUM upon UPDATE " + trophyNum);
        System.out.println("THis is trophiesBEFORE " + boolForTrophies);


        final ProgressDialog dlg = new ProgressDialog(TriviaActivity.this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Records");

        // Retrieve the object by id
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject records, ParseException e) {

                dlg.dismiss();

                if (e == null) {


                    boolForTrophies.set(trophyNum, true);
                    records.put("trophies", boolForTrophies);
                    records.saveInBackground();

                }

            }
        });

        System.out.println("THis is trophyNUM after SAVE " + trophyNum);
        System.out.println("THis is trophiesAFTER " + boolForTrophies);

    }

    public void updateCorrect() {

        corrects.add(triviaName);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Corrects");

        System.out.println("We're in RECORDS in UPDATE CORRECT");

        query.whereEqualTo("playerID", objectID);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    System.out.println("We're in DONE");
                    Log.d("Brand", "Retrieved " + objects + " Brands");

                } else {
                    System.out.println("We're in DONE2");
                    Log.d("Brand", "Error: " + e.getMessage());
                }

                if (objects == null || objects.size() == 0) {
                    System.out.println("We're in DONE3");
                    return;
                }
                ParseObject object;
                //corrects.add(triviaName);
                object = objects.get(0);
                object.put("corrects", corrects);
                object.saveInBackground();


            }
        });

        System.out.println("THis is CORRECTS after correct ANSWER given: " + corrects);

    }


    public void doTrue() {

        if (answerBool) {

            updateTrophyCount(true);

            Intent correctIntent = new Intent(getApplicationContext(), CorrectActivity.class);
            //correctIntent.putExtra("objectID", objectID);
            correctIntent.putExtra("response", response);
            startActivity(correctIntent);

            updateCorrect();

        } else {

            updateTrophyCount(false);

            Intent incorrectIntent = new Intent(getApplicationContext(), IncorrectActivity.class);
            //incorrectIntent.putExtra("objectID", objectID);
            incorrectIntent.putExtra("response", response);
            startActivity(incorrectIntent);
        }
    }

    public void doFalse() {

        if (!answerBool) {

            updateTrophyCount(true);

            Intent correctIntent = new Intent(getApplicationContext(), CorrectActivity.class);
            //correctIntent.putExtra("objectID", objectID);
            correctIntent.putExtra("response", response);
            startActivity(correctIntent);

            updateCorrect();

        } else {

            updateTrophyCount(false);

            Intent incorrectIntent = new Intent(getApplicationContext(), IncorrectActivity.class);
            //incorrectIntent.putExtra("objectID", objectID);
            incorrectIntent.putExtra("response", response);
            startActivity(incorrectIntent);
        }

    }

    public void clickCategory() {
        Intent categoriesActivityIntent = new Intent(getApplicationContext(), CategoriesActivity.class);
        //categoriesActivityIntent.putExtra("objectID", objectID);
        categoriesActivityIntent.putExtra("response", response);
        startActivity(categoriesActivityIntent);
    }

    public void clickTrophy() {
        Intent trophyIntent = new Intent(this, TrophyActivity.class);
        //trophyIntent.putExtra("objectID", objectID);
        trophyIntent.putExtra("response", response);
        startActivity(trophyIntent);
    }

    public void clickNewGame() {
        Intent newGameIntent = new Intent(this, MainActivity.class);
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
