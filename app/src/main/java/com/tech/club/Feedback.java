package com.tech.club;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class Feedback extends AppCompatActivity {

    RelativeLayout mainContent;
    private EditText feedback;
    private Button snd_fdbck;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        feedback=(EditText) findViewById(R.id.et_feedback);
        snd_fdbck=(Button)findViewById(R.id.bt_send_feedback);
        mainContent=(RelativeLayout) findViewById(R.id.main_content);

        snd_fdbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        String userFeedback= feedback.getText().toString();
                pDialog=new ProgressDialog(Feedback.this);
                pDialog.setMessage("Sending Feedback...");
                pDialog.show();


                if (userFeedback != null && !userFeedback.isEmpty()) {
                    ParseObject testObject=new ParseObject("Feedback");
                    testObject.put("user", ParseUser.getCurrentUser().getUsername());
                    testObject.put("feedback",userFeedback);

                    testObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null) {
                                Snackbar.make(mainContent, "Thank you for feedack",Snackbar.LENGTH_LONG).setAction("Dismiss", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }).show();
                                pDialog.dismiss();
                            }
                            else {
                                Snackbar.make(mainContent, "failed to upload", Snackbar.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }
                        }
                    });
                }
                else {
                    pDialog.dismiss();
                    Snackbar.make(mainContent, "Field can't be empty", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
