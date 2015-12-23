package com.tftrivia.tftrivia;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class CorrectActivity extends AppCompatActivity {

    String objectID;
    String response;
    ArrayList<Boolean> boolForTrophies;
    int trophyNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        //objectID = intent.getStringExtra("objectID");
        objectID = ((MyApplication) this.getApplication()).getObjectID();
        response = intent.getStringExtra("response");


        TextView text = (TextView) findViewById(R.id.response);
        text.setText(Html.fromHtml(response));


        final ProgressDialog dlg = new ProgressDialog(CorrectActivity.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Checking for new_game");
        //dlg.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Records");

        // Retrieve the object by id
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject records, ParseException e) {
                dlg.dismiss();
                if (e == null) {

                    System.out.println("THis is TROPHYNUM in CORRECT1 " + trophyNum);

                    trophyNum = (int) records.get("trophyNum");
                    trophyNum = (int) records.get("trophyNum");
                    boolForTrophies = (ArrayList<Boolean>) records.get("trophies");
                    records.put("trophyNum", -1);
                    records.saveInBackground();


                    setOnClick();

                    System.out.println("THis is TROPHYNUM in CORRECT3 " + trophyNum);

                }
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

    public void setOnClick() {

        (findViewById(R.id.screen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent winIntent;
                switch (trophyNum) {
                    case 0:
                        winIntent = new Intent(getApplicationContext(), WinActivity.class);
                        //winIntent.putExtra("objectID", objectID);
                        //winIntent.putExtra("catResponse", catResponse);
                        winIntent.putExtra("win", "\nYou earned your first trophy! \n<i>The Pyramid</i>");
                        winIntent.putExtra("trophy", 1);
                        startActivity(winIntent);
                        break;
                    case 1:
                        winIntent = new Intent(getApplicationContext(), WinActivity.class);
                        //winIntent.putExtra("objectID", objectID);
                        //winIntent.putExtra("catResponse", catResponse);
                        winIntent.putExtra("win", " You earned the \n<i>Cuboctahedron!</i>");
                        winIntent.putExtra("trophy", 2);
                        startActivity(winIntent);
                        break;
                    case 2:
                        winIntent = new Intent(getApplicationContext(), WinActivity.class);
                        //winIntent.putExtra("objectID", objectID);
                        //winIntent.putExtra("catResponse", catResponse);
                        winIntent.putExtra("win", " You earned the \n<i>Icosahedron!</i>");
                        winIntent.putExtra("trophy", 3);
                        startActivity(winIntent);
                        break;
                    case 3:
                        winIntent = new Intent(getApplicationContext(), WinActivity.class);
                        //winIntent.putExtra("objectID", objectID);
                        //winIntent.putExtra("catResponse", catResponse);
                        winIntent.putExtra("win", " You earned the \n<i>Star Tetrahedron!</i>");
                        winIntent.putExtra("trophy", 4);
                        startActivity(winIntent);
                        break;
                    default:
                        Intent triviaIntent = new Intent(getApplicationContext(), TriviaActivity.class);
                        //triviaIntent.putExtra("objectID", objectID);
                        //triviaIntent.putExtra("catResponse", catResponse);
                        System.out.println("We're doing the default from CORRECT");
                        startActivity(triviaIntent);
                        break;
                }

            }
        });
    }

    public void clickCategory() {
        Intent categoriesActivityIntent = new Intent(getApplicationContext(),CategoriesActivity.class);
        //categoriesActivityIntent.putExtra("objectID", objectID);
        //categoriesActivityIntent.putExtra("catResponse", catResponse);
        startActivity(categoriesActivityIntent);
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
