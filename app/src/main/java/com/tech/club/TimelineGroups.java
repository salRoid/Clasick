package com.tech.club;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class TimelineGroups extends Fragment {


    private ListView user_grp_list;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Profile activity=(Profile)getActivity();
        ParseObject user_object=activity.sendmyobject();


        View view = inflater.inflate(R.layout.activity_timeline_groups, container, false);
        user_grp_list=(ListView)view.findViewById(R.id.timeline_user_group);

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Group");
        query.whereEqualTo("members",user_object);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() > 0) {

                    String user_groups[] = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        user_groups[i]=list.get(i).getString("name");
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,user_groups);
                    user_grp_list.setAdapter(adapter);
                    user_grp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(getActivity(),GroupPage.class);

                            String msg= ((TextView) view).getText().toString();
                            intent.putExtra("grp_name", msg);
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    });
                }

                else{

                }
            }
        });



        return view;
    }
}
