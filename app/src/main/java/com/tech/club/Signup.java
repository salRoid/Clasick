package com.tech.club;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import Fragments.DashboardFragment;


public class Signup extends AppCompatActivity implements View.OnClickListener {
    EditText setusername, setpasswordonce, setpasswordtwice, setemail;
    private Button fSignup;
    private ProgressDialog pdialog;
    RadioButton Rstudent,Rteacher;
    String who=" ";
    RelativeLayout mainContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fSignup = (Button) findViewById(R.id.finishSignup);
        fSignup.setOnClickListener(this);

        Rstudent= (RadioButton) findViewById(R.id.student_check);
        Rteacher= (RadioButton) findViewById(R.id.teacher_check);

        mainContent=(RelativeLayout)findViewById(R.id.main_content);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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

    @Override
    public void onClick(View v) {
        setusername = (EditText) findViewById(R.id.set_user_id);
        final String Setusername = setusername.getText().toString();

        setpasswordonce = (EditText) findViewById(R.id.pass_once);
        final String Passwordonce = setpasswordonce.getText().toString();

        setpasswordtwice = (EditText) findViewById(R.id.pass_twice);
        String Passwordtwice = setpasswordtwice.getText().toString();

        setemail = (EditText) findViewById(R.id.email);
        String Email = setemail.getText().toString();


        if (!(Setusername.length() > 0) || !(Passwordonce.length() > 0) || !(Passwordtwice.length() > 0) || !(Email.length() > 0) || !(Rstudent.isChecked()|| Rteacher.isChecked()))
        {

            Snackbar.make(mainContent, "Parameters not filled.", Snackbar.LENGTH_SHORT).show();
        }

        else
        {
            if (!(Passwordonce.equals(Passwordtwice))) {
                Snackbar.make(mainContent, "Password didn't match", Snackbar.LENGTH_SHORT).show();
            } else {
                pdialog = new ProgressDialog(this);
                pdialog.setCancelable(false);
                pdialog.setMessage("Signing Up ..");
                pdialog.show();


                if (Rstudent.isChecked()) {
                    who = "Student";
                } else if (Rteacher.isChecked()) {
                    who = "Teacher";
                }

                final ParseUser pUser = new ParseUser();
                pUser.setUsername(Setusername);
                pUser.setPassword(Passwordonce);
                pUser.put("email", Email);
                pUser.put("who", who);
                pUser.put("query_count",0);


                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.whereEqualTo("username", Setusername);

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {


                        if (e == null) {
                            if (!(parseUsers.size() > 0)) {

                                pUser.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if (e == null) {
                                            pdialog.dismiss();

                                            SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putBoolean("logged_in", true);
                                            editor.putString("who", who);
                                            editor.putString("user_logged", Setusername);
                                            editor.commit();
                                            Snackbar.make(mainContent, "Sign up completed.", Snackbar.LENGTH_SHORT).show();
                                            Intent i = new Intent(Signup.this, UserImage.class);
                                            startActivity(i);
                                            /*Intent i = new Intent(Signup.this, UserImage.class);
                                            startActivity(i);*/
                                        } else {
                                            pdialog.dismiss();
                                            Snackbar.make(mainContent, e.getMessage().toString(), Snackbar.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            } else {

                                //user already exist
                                pdialog.dismiss();
                                Snackbar.make(mainContent, "User already exist.", Snackbar.LENGTH_SHORT).show();

                            }
                        } else {
                            pdialog.dismiss();
                            Snackbar.make(mainContent, e.getMessage().toString(), Snackbar.LENGTH_SHORT).show();
                            //errror
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,Login.class));
        super.onBackPressed();
    }
}
