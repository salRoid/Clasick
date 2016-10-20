package com.tech.club;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

public class GroupMembers extends AppCompatActivity {

    private String grp_name;
    private ListView list_member;
    private String grp_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);

        list_member = (ListView) findViewById(R.id.member_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            grp_name = intent.getStringExtra("grp_name");
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("name", grp_name);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                grp_admin=parseObject.getString("admin");

                ParseRelation<ParseObject> relation = parseObject.getRelation("members");
                ParseQuery<ParseObject> query1 = relation.getQuery();
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null && list.size() > 0) {
                            String member_queries[] = new String[list.size()];

                            for (int i = 0; i < list.size(); i++) {
                                member_queries[i] = list.get(i).getString("username");
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GroupMembers.this, android.R.layout.simple_list_item_1, member_queries);
                            list_member.setAdapter(adapter);
                            list_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent=new Intent(GroupMembers.this,Profile.class);
                                    String user=((TextView) view).getText().toString();
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                }
                            });



                        } else {

                        }
                    }
                });


            }
        });

    }

}
