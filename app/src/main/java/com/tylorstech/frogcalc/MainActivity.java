package com.tylorstech.frogcalc;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements  Calculator.OnFragmentInteractionListener,
                                                                TemperatureConverterFragment.OnFragmentInteractionListener,
                                                                AboutFragment.OnFragmentInteractionListener
{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    NavigationView bottomNavigationView;
    NavigationItemSelectedListener navigationItemSelectedListener;

    /*
    TODO:
        (done) make that imagebutton white
        (done) add support for the period/dot/whatever
        (done) buzz on button press
        add history by adding calculationText + result to a stack every time the equals button is pressed
        (done) make a splash screen
        comment code
        provide further user interaction (Could include sound, flashing, highlighting, color, etc. )
        make sure adapting properly to screen size changes
        Flash window that shows your name and a welcome message as it starts.
        (done) About screen
        Allow switch between decimal, hex, and octal, and binary
        Allow switch between normal, fixed point, and scientific notation
        "Settings" screen: can set decimals places, coloring options for negative values, etc.
        (done) add temperature converter


        BUGS
        type text with operators, press c, will keep operator on top and also a calculation
        crashes when dividing by zero
        dot and then negative sign crashes app

     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        bottomNavigationView = (NavigationView) findViewById(R.id.navigation_drawer_bottom);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Calculator");

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame_layout, new Calculator());
        fragmentTransaction.commit();

        navigationItemSelectedListener = new NavigationItemSelectedListener();
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
    }


    class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId()){
                case R.id.nav_calc:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame_layout, new Calculator());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Calculator");
                    drawerLayout.closeDrawers();
                    break;

                case R.id.nav_thermometer:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame_layout, new TemperatureConverterFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Temperature Converter");
                    drawerLayout.closeDrawers();
                    break;

                case R.id.nav_about:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame_layout, new AboutFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("About Frogcalc");
                    drawerLayout.closeDrawers();
                    break;

                case R.id.nav_settings:

                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
