package com.tech.club;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ParseWorks.DisplayImage;


public class Profile extends AppCompatActivity {
    private static CircularImageView timeline;
    private TextView timeline_user;
    private TextView timeline_tag;
    private static Toolbar toolbar;
    private CardView cv;
    private static ImageView iv;
    private static RelativeLayout timeline_box;
    static String mylatesturl;
    private static TabLayout tabLayout;
    private String timeline_username;
    private String user_tag;
    private ParseObject user_object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            overridePendingTransition(R.anim.left_in, R.anim.left_out);


        Transition renterTrans=new Slide();
        getWindow().setReenterTransition(renterTrans);



        if(Build.VERSION.SDK_INT>=21){
            getWindow().setSharedElementExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
        }

        setContentView(R.layout.activity_profile);


         toolbar = (Toolbar) findViewById(R.id.profile_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        timeline = (CircularImageView) findViewById(R.id.timeline_profile_pic);
        timeline_tag = (TextView) findViewById(R.id.timeline_tagline);
        timeline_box=(RelativeLayout)findViewById(R.id.profile_cache);
        cv=(CardView)findViewById(R.id.cv_status);


       /* iv = (ImageView) findViewById(R.id.test);*/






        Intent intent = getIntent();
        if (intent != null) {
            timeline_username = intent.getStringExtra("user");
        }

        setTitle(timeline_username);


        ParseQuery<ParseUser> query =ParseUser.getQuery();
        query.whereEqualTo("username", timeline_username);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                 user_object=parseUser;
                    user_tag = parseUser.getString("tagline");
                    cv.setVisibility(View.VISIBLE);
                    timeline_tag.setText(user_tag);



                } else {

           }


            }
        });



        // timeline_user.setText(timeline_username);



        DisplayImage.retreivetimelineprofile(timeline_username, Profile.this);



        if(timeline_username.contains(ParseUser.getCurrentUser().getUsername())) {

            timeline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(Profile.this, timeline_tag, timeline_tag.getTransitionName());


                    Intent intent =new Intent(Profile.this,ChangeProfile.class);
                    intent.putExtra("profileurl",mylatesturl);
                    intent.putExtra("tagline",user_tag);
                    startActivity(intent,optionsCompat.toBundle());
                }
            });

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==android.R.id.home){
          super.onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_profile, menu);
        return true;
    }


    public static void retriveTimelineProfileCallback(Context context, String url) {
        mylatesturl=url;

        /*Picasso.with(context).load(url).transform(new BlurTransform(context)).into(iv);*/

        Picasso.with(context).load(url).placeholder(R.drawable.user1).error(R.drawable.user1).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                timeline.setImageBitmap(bitmap);


                Palette palette = Palette.from(bitmap).generate();
                Palette.Swatch swatch = palette.getLightVibrantSwatch();
                if (swatch != null) {
                    timeline_box.setBackgroundColor(swatch.getRgb());
                    tabLayout.setSelectedTabIndicatorColor(swatch.getRgb());


                } else
                    swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    timeline_box.setBackgroundColor(swatch.getRgb());
                    tabLayout.setSelectedTabIndicatorColor(swatch.getRgb());
                }
                else
                    swatch=palette.getMutedSwatch();
                if (swatch!=null) {
                    timeline_box.setBackgroundColor(swatch.getRgb());
                    tabLayout.setSelectedTabIndicatorColor(swatch.getRgb());
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }



    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new Timeline(), "Timeline");
        adapter.addFragment(new TimelineQuery(), "Query");
        adapter.addFragment(new TimelineGroups(), "Groups");
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    public String sendmydata(){
        return timeline_username;
    }

    public ParseObject sendmyobject(){
        return user_object;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

}
