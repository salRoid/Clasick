package com.tech.club;

import android.app.Application;

import com.parse.Parse;

public class ClassifyApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "4rIpmaO8yCdDk5z1ICy8BpHfqPaqOto7s5RoRIvO", "ab933k57jjfidHyOgQGzZHQQ0vfzxejB5XBz5WhJ");


    }
}
