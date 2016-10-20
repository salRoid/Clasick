package com.tech.club;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import Data.SetterGetterQuestions;
import ParseWorks.QAworks;


public class QuesSending extends AppCompatActivity implements View.OnClickListener{


    EditText ques;
    Button send_ques;
    int number=0;
    String cheked_grp;
    private ProgressDialog pDialog;
    private RadioGroup radio_grp;
    private ListView grp_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ques_sending);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ques = (EditText) findViewById(R.id.ques_text);
        send_ques=(Button) findViewById(R.id.ask);
        send_ques.setOnClickListener(this);

        grp_list=(ListView)findViewById(R.id.grp_list);


      /*  ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("members", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() > 0) {

                    String user_groups[] = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        user_groups[i] = list.get(i).getString("name");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(QuesSending.this, android.R.layout.simple_list_item_multiple_choice, user_groups);
                    grp_list.setAdapter(adapter);
                    grp_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                    grp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            grp_list.setItemChecked(position, true);
                            cheked_grp = ((TextView) view).getText().toString();

                        }
                    });
                } else {

                }
            }
        });*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ques_sending, menu);
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
            startActivity(new Intent(this,ChangePassword.class));
        }
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    /*public static void retriveUrlCallback(Context context, String url) {
       // imageUrl= url;
    }*/


    @Override
    public void onClick(View v) {
        final String question = ques.getText().toString();

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Uploading...");
        pDialog.show();

        SharedPreferences sp=getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        final String asked_by=sp.getString("user_logged", " ");


        if (question != null && !question.isEmpty()) {

            final ParseObject testObject = new ParseObject("Ques");
            testObject.put("question", question);
            testObject.put("User", asked_by);
            testObject.put("answer", number);

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.include("Profile");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.getFirstInBackground(new GetCallback<ParseUser>() {

                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    ParseObject po = parseUser.getParseObject("Profile");
                    testObject.put("user_profile",po);
                    testObject.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                ParseQuery<ParseUser> query = ParseUser.getQuery();
                                query.whereEqualTo("username", asked_by);
                                query.getFirstInBackground(new GetCallback<ParseUser>() {
                                    @Override
                                    public void done(ParseUser parseUser, ParseException e) {
                                        if (e == null) {
                                            parseUser.increment("query_count");
                                        } else {

                                        }


                                    }
                                });


                                pDialog.dismiss();


                                Toast.makeText(QuesSending.this, "uploaded", Toast.LENGTH_SHORT).show();
                                finish();



                                SetterGetterQuestions newSGQ = new SetterGetterQuestions();
                                newSGQ.setQuestion(question);
                                newSGQ.setUser(asked_by);
                                newSGQ.answerCount("0");
                                newSGQ.setObject_id(testObject.getObjectId());
                                newSGQ.setTime(testObject.getCreatedAt().toString());

                                QAworks.refreshQuestions(QuesSending.this);

                                Ques.sgQuestionsArrayList.add(0, newSGQ);

                                if (Ques.adapter != null)
                                    Ques.adapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(QuesSending.this, "failed to upload "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }
                        }
                    });
                }
            });


            //testObject.put("answers","This is my answer");







        }
        else {
            pDialog.dismiss();
            Toast.makeText(QuesSending.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
        }
    }


}
