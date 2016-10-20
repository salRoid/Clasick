package com.tech.club;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ClassifyGroups extends AppCompatActivity {

    private String user_desig;
    private Button bt_create;
    private Button bt_search;
    private EditText et_search;
    private String to_search;
    private ProgressBar search_bar;
    private EditText et_create;
    private String grpname;
    private CardView cv_searchresult;
    private TextView tv_searched;
    private String grpdesc;
    private EditText et_desc;
    private String grpadmin;
    private CardView cv_create;

    private TextView tv_nothing;
    private FloatingActionButton bt_make;
    private Button bt_check;
    boolean confirmation=false;
    private String grpname_check=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_groups);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        bt_check=(Button)findViewById(R.id.check_primary);
        tv_nothing=(TextView)findViewById(R.id.nothing);
        bt_create = (Button) findViewById(R.id.bt_create);
        bt_search = (Button) findViewById(R.id.bt_search);
        et_search = (EditText) findViewById(R.id.et_search);
        et_create = (EditText) findViewById(R.id.grp_name);
        bt_make=(FloatingActionButton)findViewById(R.id.maker);
        cv_searchresult = (CardView) findViewById(R.id.my_serachcard);
        search_bar = (ProgressBar) findViewById(R.id.my_bar);
        tv_searched=(TextView)findViewById(R.id.searched);
        et_desc=(EditText)findViewById(R.id.grp_desc);
        cv_create=(CardView)findViewById(R.id.cv_create);





        SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);

        user_desig = sp.getString("who", " ");

        if (user_desig.contains("Student")) {
            bt_make.setVisibility(View.INVISIBLE);
        }
        if (user_desig.contains("Teacher")) {
            bt_make.setVisibility(View.VISIBLE);
        }


        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grpname_check=et_create.getText().toString();
                if(!(grpname_check.length()>0)){
                    Toast.makeText(ClassifyGroups.this,"enter group name",Toast.LENGTH_SHORT).show();
                }
                else{
                    ParseQuery<ParseObject> check=ParseQuery.getQuery("Group");
                    check.whereEqualTo("name",grpname_check);
                    check.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e==null){
                                confirmation=false;
                                bt_check.setBackgroundColor(Color.RED);
                                grpname_check=null;
                            }
                            else{
                                bt_check.setBackgroundColor(Color.GREEN);
                                confirmation=true;

                            }
                        }
                    });
                }
            }
        });






        bt_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_make.setVisibility(View.INVISIBLE);
                cv_create.setVisibility(View.VISIBLE);
                cv_searchresult.setVisibility(View.INVISIBLE);

            }
        });



        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject testObject = new ParseObject("Group");
                grpname = et_create.getText().toString();
                grpdesc=et_desc.getText().toString();
                if(!(grpname.length()>0) || !(grpdesc.length()>0)){
                    Toast.makeText(ClassifyGroups.this, "parameters not filled", Toast.LENGTH_SHORT).show();
                }

                else {

                    if(grpname.contains(grpname_check) && confirmation==true){
                        testObject.put("name", grpname);
                        testObject.put("desc",grpdesc);
                        testObject.put("admin", ParseUser.getCurrentUser().getUsername());
                        ParseRelation<ParseObject> relation = testObject.getRelation("members");
                        relation.add(ParseUser.getCurrentUser());
                        testObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Intent intent = new Intent(ClassifyGroups.this, GroupPage.class);
                                    intent.putExtra("grp_name", grpname);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ClassifyGroups.this, "not done", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    else{
                        Toast.makeText(ClassifyGroups.this,"shut up",Toast.LENGTH_SHORT).show();
                    }


                }

            }

        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_search = et_search.getText().toString();
                if (to_search.length()>0) {
                    tv_nothing.setVisibility(View.INVISIBLE);
                    cv_searchresult.setVisibility(View.VISIBLE);
                    search_bar.setVisibility(View.VISIBLE);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
                    query.whereContains("name", to_search);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                to_search= parseObject.getString("name");
                                tv_searched.setText(to_search);
                                search_bar.setVisibility(View.INVISIBLE);
                                cv_searchresult.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ClassifyGroups.this, GroupPage.class);
                                        intent.putExtra("grp_name", to_search);
                                        startActivity(intent);

                                    }
                                });

                            } else {
                                tv_nothing.setVisibility(View.VISIBLE);
                                search_bar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(ClassifyGroups.this, "never Blank", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_classify_groups, menu);
        return true;
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
        if (id == R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
