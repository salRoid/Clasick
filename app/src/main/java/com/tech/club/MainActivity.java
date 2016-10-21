package com.tech.club;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import CustomViews.SlidingTabLayout;
import ParseWorks.DisplayImage;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static ImageView profile;
    TextView User;
    TextView designation;
    ActionBarDrawerToggle abdt;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private String user_desig;

    public static void retriveProfileCallback(Context context, String url) {
        Picasso.with(context).load(url).placeholder(R.drawable.user2).error(R.drawable.user2).into(profile);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        profile =(ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_pic);

        DisplayImage.retreiveprofile(ParseUser.getCurrentUser().getUsername(), MainActivity.this);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        User = (TextView)  navigationView.getHeaderView(0).findViewById(R.id.current_user);
        User.setText(ParseUser.getCurrentUser().getUsername());

        designation = (TextView) navigationView.getHeaderView(0).findViewById(R.id.current_desig);


        SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);

        user_desig = sp.getString("who", " ");
        designation.setText(user_desig);


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.msliding_tabs);
        slidingTabLayout.setViewPager(viewPager);


        /*NavigationDrawerfragment drawerFragment = (NavigationDrawerfragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);*/

        abdt = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(abdt);
        abdt.syncState();


        installupdate();

    }

    /*private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
*/

    private void installupdate() {
        ParseInstallation pinst = ParseInstallation.getCurrentInstallation();
        ParseUser puser = ParseUser.getCurrentUser();
        if (puser != null) {
            pinst.put("username", puser.getUsername());
            pinst.saveInBackground();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ChangePassword.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        abdt.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {


        if (menuItem.getItemId() == R.id.nav_profile) {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent =new Intent(MainActivity.this,Profile.class);
            intent.putExtra("user", ParseUser.getCurrentUser().getUsername());
            startActivity(intent);
            return true;
        }
        if (menuItem.getItemId() == R.id.nav_about) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, AboutApp.class));
            return true;
        }
        if(menuItem.getItemId()==R.id.nav_home){
            menuItem.setChecked(true);

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        if(menuItem.getItemId()==R.id.nav_messages){
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, Feedback.class));
            return true;
        }
        if (menuItem.getItemId() == R.id.nav_logout) {

            ParseUser.getCurrentUser().logOutInBackground();

            SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("logged_in", false);
            editor.commit();
            finish();
            startActivity(new Intent(this,Login.class));

            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else
        super.onBackPressed();
    }
}
