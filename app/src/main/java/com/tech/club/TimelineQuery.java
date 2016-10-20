package com.tech.club;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class TimelineQuery extends Fragment {


    private Context context;
    private ListView user_query_list;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Profile activity=(Profile)getActivity();
        String selected_user=activity.sendmydata();


        View view = inflater.inflate(R.layout.activity_timeline_query, container, false);


        user_query_list=(ListView)view.findViewById(R.id.timeline_user_query);

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Ques");
       query.whereEqualTo("User", selected_user);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() > 0) {

                    String user_queries[]=new String[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        user_queries[i]=list.get(i).getString("question");
                    }

                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,user_queries);
                    user_query_list.setAdapter(adapter);
                }
                else{

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
