package com.tech.club;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import Adapters.QuestionsListAdapter;
import Data.SetterGetterQuestions;
import ParseWorks.QAworks;

public class GroupPage extends AppCompatActivity {


    private String grp_name;
    private static String grp_desc;
    private TextView descript;
    private String grp_admin;
    FloatingActionButton fab;
    private Button bt_edit_desc;
    private boolean joined;
    private ListView ques_list;
    private ListView upload_list;
    private TextView query2_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);


/*
        ques_list=(ListView)findViewById(R.id.grp_ques_list);
        upload_list=(ListView)findViewById(R.id.grp_upload_list);*/

        fab=(FloatingActionButton)findViewById(R.id.join_fab);
        bt_edit_desc=(Button)findViewById(R.id.edit_desc);
        descript=(TextView)findViewById(R.id.wr_desc);
      /*  query2_c=(TextView)findViewById(R.id.query_c);*/


        Intent intent = getIntent();
        if (intent != null) {
            grp_name = intent.getStringExtra("grp_name");
        }

        ParseQuery<ParseObject> check=ParseQuery.getQuery("Group");
        check.whereEqualTo("name",grp_name);
        check.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                ParseRelation<ParseObject> relation = parseObject.getRelation("members");
                ParseQuery<ParseObject> query1 = relation.getQuery();
                query1.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                query1.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if(e==null){
                            fab.setImageResource(R.drawable.ic_done_white_24dp);
                            joined=true;
                        }
                        else{
                            fab.setImageResource(R.drawable.ic_add_white_24dp);
                            joined=false;
                        }
                    }
                });
            }
        });






        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(grp_name);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("name", grp_name);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    grp_admin = parseObject.getString("admin");
                    grp_desc = parseObject.getString("desc");
                    descript.setText(grp_desc);

                    if (ParseUser.getCurrentUser().getUsername().equals(grp_admin)) {
                        fab.setImageResource(R.drawable.ic_done_white_24dp);
                        bt_edit_desc.setVisibility(View.VISIBLE);
                    } else
                        bt_edit_desc.setVisibility(View.INVISIBLE);

                    bt_edit_desc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(GroupPage.this, "Update still in progress", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else
                    Toast.makeText(GroupPage.this, "dbd", Toast.LENGTH_SHORT).show();
            }
        });


       /* ParseQuery<ParseObject> ques_query=ParseQuery.getQuery("Ques");
        ques_query.whereEqualTo("group",grp_name);
        ques_query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
           int n=list.size();
                query2_c.setText(n);
            }
        });*/


       /* ParseQuery<ParseObject> ques_query=ParseQuery.getQuery("Ques");
        ques_query.whereEqualTo("group",grp_name);
        ques_query.orderByDescending("createdAt");
        ques_query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() > 0) {
                    String ques_queries[] = new String[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        ques_queries[i] = list.get(i).getString("question");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GroupPage.this, android.R.layout.simple_list_item_1, ques_queries);
                    ques_list.setAdapter(adapter);
                } else {

                }
            }
        });

        ParseQuery<ParseObject> upload_query=ParseQuery.getQuery("Upload");
        upload_query.whereEqualTo("group",grp_name);
        upload_query.orderByDescending("createdAt");
        upload_query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() > 0) {
                    String upload_queries[] = new String[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        upload_queries[i] = list.get(i).getString("title");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GroupPage.this, android.R.layout.simple_list_item_1, upload_queries);
                    upload_list.setAdapter(adapter);
                } else {

                }
            }
        });
*/




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
                query.whereEqualTo("name", grp_name);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {

                            ParseRelation<ParseObject> relation = parseObject.getRelation("members");

                            if(joined==false) {
                                relation.add(ParseUser.getCurrentUser());
                                fab.setImageResource(R.drawable.ic_done_white_24dp);
                            }
                            else{
                                relation.remove(ParseUser.getCurrentUser());
                                fab.setImageResource(R.drawable.ic_add_white_24dp);
                            }
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(GroupPage.this, "done", Toast.LENGTH_SHORT);
                                    } else {

                                    }
                                }
                            });
                        }
                    }
                });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.members){
            Intent intent=new Intent(GroupPage.this,GroupMembers.class);
            intent.putExtra("grp_name",grp_name);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}




