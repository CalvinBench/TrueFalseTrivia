package com.tftrivia.tftrivia;

import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by Geometrix on 12/7/15.
 */
public class DrawerActivity extends AppCompatActivity {

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private boolean opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_toolbar);

        //Remove line to test RTL support
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Categories").withDescription("Choose from 6!").withIcon(getResources().getDrawable(R.drawable.categories));
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Trophies").withDescription("View the trophies you've earned").withIcon(getResources().getDrawable(R.drawable.trophies));
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("How To").withDescription("Get the details on gameplay").withIcon(getResources().getDrawable( R.drawable.trophies));
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("New Game").withDescription("Your current game will be lost").withIcon(getResources().getDrawable( R.drawable.new_game));
        DividerDrawerItem item5 = new DividerDrawerItem();
        SwitchDrawerItem item6 = new SwitchDrawerItem().withName("Sound").withIcon(getResources().getDrawable(R.drawable.trophy_soul)).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener);



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

    }


    public void clickCategory() {
        Intent categoriesActivityIntent = new Intent(getApplicationContext(),CategoriesActivity.class);
        startActivity(categoriesActivityIntent);
    }

    public void clickTrophy() {
        Intent trophyIntent = new Intent( this, TrophyActivity.class);
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
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };


}
