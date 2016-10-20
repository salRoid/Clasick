package com.tech.club;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;


public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText userId, epassword;
    private Button signUp;
    private Button loginbutton, forgotbutton;
    private ProgressDialog pdialog;
    private Boolean loggedIn = false;
    String who = " ";
    private RelativeLayout mainContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        loginbutton = (Button) findViewById(R.id.Loginbut);
        loginbutton.setOnClickListener(this);


        signUp = (Button) findViewById(R.id.signup);
        signUp.setOnClickListener(this);


        forgotbutton = (Button) findViewById(R.id.forgot);
        forgotbutton.setOnClickListener(this);


        mainContent=(RelativeLayout)findViewById(R.id.main_content);




        String udata = "Help?";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);

        forgotbutton.setText(content);


            SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);

            loggedIn = sp.getBoolean("logged_in", false);

        if (loggedIn)
            startActivity(new Intent(this, MainActivity.class));



    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup) {
            Intent i = new Intent(this, Signup.class);
            startActivity(i);
        }
        if (v.getId() == R.id.forgot) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Password Recovery");
            alertDialog.setMessage("Enter e-mail");
            final EditText input = new EditText(this);


            FrameLayout container = new FrameLayout(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 30;
            params.rightMargin = 50;
            input.setLayoutParams(params);
            container.addView(input);


            alertDialog.setView(container);
            alertDialog.setPositiveButton("OKAY",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                            final String email = input.getText().toString();

                            if (!(email.length() > 0)) {
                                Snackbar.make(mainContent,"Fields not filled.",Snackbar.LENGTH_LONG).show();


                            } else {
                                pdialog = new ProgressDialog(Login.this);
                                pdialog.setMessage("Sending recovery mail..");
                                pdialog.show();



                                ParseUser.requestPasswordResetInBackground(email,
                                        new RequestPasswordResetCallback() {
                                            public void done(ParseException e) {

                                                pdialog.dismiss();

                                                if (e == null) {

                                                    Toast.makeText(Login.this, "Check your mail to recover password.", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    Toast.makeText(Login.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                );

                            }
                        }
                    });

            alertDialog.setNegativeButton("CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            dialog.cancel();
                        }
                    });

            alertDialog.show();

        }

        if (v.getId() == R.id.Loginbut) {


            userId = (EditText) findViewById(R.id.username);

            String loguserid = userId.getText().toString();
            epassword = (EditText) findViewById(R.id.password);

            String logpassword = epassword.getText().toString();


            if (!(loguserid.length() > 0) || !(logpassword.length() > 0)) {
                Snackbar snackbar=Snackbar.make(mainContent, "FIELDS NOT FILLED.", Snackbar.LENGTH_SHORT);
                View snackbarview=snackbar.getView();
                snackbarview.setBackgroundColor(Color.WHITE);
                TextView textView = (TextView) snackbarview.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.BLACK);
                textView.setTypeface(null,Typeface.BOLD);
                snackbar.show();
            } else {
                pdialog = new ProgressDialog(this);
                pdialog.setCancelable(false);
                pdialog.setMessage("Please wait ..");
                pdialog.show();


                ParseUser.logInInBackground(loguserid, logpassword, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (parseUser != null) {

                            who = parseUser.get("who").toString();


                            pdialog.dismiss();


                            SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("logged_in", true);
                            editor.putString("who", who);
                            editor.putString("user_logged", parseUser.getUsername());
                            editor.commit();

                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);

                        } else {
                            pdialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("Oops").
                                    setMessage(e.getMessage().toString()).setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();


                           /* Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();*/
                        }
                    }
                });
            }
        }
    }

}