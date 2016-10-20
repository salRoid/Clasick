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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import Adapters.AnswersListAdapter;
import Data.SetterGetterAnswers;
import ParseWorks.Answers;
import ParseWorks.QAworks;



public class AnswerShow extends AppCompatActivity implements View.OnClickListener {


    /*static ArrayAdapter<String> adapter;*/
    static AnswersListAdapter adapter;
    static ListView answerList;
    static TextView set_user;
    static TextView set_question;
    static List<SetterGetterAnswers> sgAnswersArrayList = null;
    static Context context;
    static TextView first;
    static ImageView first_img;
    private static ProgressBar spinner;
    EditText ans;
    ProgressDialog pDialog;
    Button b1;
    int position;
    String objectId;
    private String question = "";
    private String qUser;
    private String up_time;
    private TextView set_time;

    public static void retrieveAnswersCallback(List<SetterGetterAnswers> setterGetterAnswersArrayList, boolean success) {

        if (success) {

            sgAnswersArrayList = setterGetterAnswersArrayList;
            adapter = new AnswersListAdapter(context, setterGetterAnswersArrayList);
            answerList.setAdapter(adapter);
            spinner.setVisibility(View.INVISIBLE);
        } else {
            first_img.setVisibility(View.VISIBLE);
            first.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_show);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        context = AnswerShow.this;


        first = (TextView) findViewById(R.id.first_Answer);
        first_img = (ImageView) findViewById(R.id.answer_img);
        ans = (EditText) findViewById(R.id.answer_text);
        set_user = (TextView) findViewById(R.id.User);
        set_question = (TextView) findViewById(R.id.question);
        set_time = (TextView) findViewById(R.id.time);
        answerList = (ListView) findViewById(R.id.answer_list);

        b1 = (Button) findViewById(R.id.send);
        b1.setOnClickListener(this);


        Intent intent = getIntent();
        if (intent != null) {
            qUser = intent.getStringExtra("user");
            question = intent.getStringExtra("question");
            objectId = intent.getStringExtra("object_id");
            up_time = intent.getStringExtra("time");
        }


        set_question.setText(question);
        set_user.setText(qUser);
        set_time.setText(up_time);

        spinner = (ProgressBar) findViewById(R.id.bar);
        spinner.setVisibility(View.VISIBLE);

        Answers.retreieveAnswers(objectId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer_show, menu);
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
    public void onClick(View v) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading answer...");
       /* pDialog.show();*/

        SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        String sent_by = sp.getString("user_logged", " ");

        final String answer = ans.getText().toString();


        if (answer != null && !answer.isEmpty()) {
            ans.setText("");

            View view = this.getCurrentFocus();
            if(view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
            ParseObject testObject = new ParseObject("Answers");
            testObject.put("answer", answer);

            if (objectId != null)
                testObject.put("quesObjId", objectId);

            testObject.put("User", sent_by);


            testObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ques");
                        query.getInBackground(objectId, new GetCallback<ParseObject>() {
                            @Override
                            public void done(final ParseObject parseObject, ParseException e) {

                                if (e == null && parseObject != null) {

                                    parseObject.increment("answer");
                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                           /* pDialog.dismiss();*/
                                            Answers.retreieveAnswers(objectId
                                            );
                                            first_img.setVisibility(View.INVISIBLE);
                                            first.setVisibility(View.INVISIBLE);
                                            Toast.makeText(AnswerShow.this, "Answer uploaded.", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                } else {
                                   /* pDialog.dismiss();*/
                                    Toast.makeText(AnswerShow.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } else {
                        Toast.makeText(AnswerShow.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                     /*   pDialog.dismiss();*/
                    }
                }
            });


        } else {
            pDialog.dismiss();
            Toast.makeText(AnswerShow.this, "Can't give a blank answer.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        QAworks.refreshQuestions(AnswerShow.this);
        super.onBackPressed();
    }
}


