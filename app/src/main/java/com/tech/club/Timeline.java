package com.tech.club;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Timeline extends Fragment {

    private TextView timeline_user_email;
    private TextView timeline_question_count;
    private String user_email;
    private String user_question_count;
    private TextView timeline_designation;
    private String user_desig;
    private String user_name;
    private TextView timeline_usernmae;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Profile activity = (Profile) getActivity();
        String selected_user = activity.sendmydata();

        View view = inflater.inflate(R.layout.activity_timeline, container, false);

        timeline_user_email = (TextView) view.findViewById(R.id.tv_usermail);
        timeline_question_count = (TextView) view.findViewById(R.id.question_count);
        timeline_designation = (TextView) view.findViewById(R.id.tv_timeline_desig);
        timeline_usernmae = (TextView) view.findViewById(R.id.set_user_orignalname);


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", selected_user);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    user_name = parseUser.getString("Name");
                    user_desig = parseUser.getString("who");
                    user_email = parseUser.getEmail();
                    user_question_count = parseUser.get("query_count").toString();

                    timeline_usernmae.setText(user_name);
                    timeline_designation.setText(user_desig);
                    timeline_user_email.setText(user_email);
                    timeline_question_count.setText(user_question_count);

                } else {

                }

            }

        });

        return view;
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





        return super.onOptionsItemSelected(item);
    }
}
