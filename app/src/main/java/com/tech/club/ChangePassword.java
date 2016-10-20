package com.tech.club;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ChangePassword extends AppCompatActivity {

    EditText old_pass, new_pass1, new_pass2;
    Button change;
    private ProgressDialog pdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        change = (Button) findViewById(R.id.change);
        old_pass = (EditText) findViewById(R.id.old_pass);
        new_pass1 = (EditText) findViewById(R.id.new_pass1);
        new_pass2 = (EditText) findViewById(R.id.new_pass2);

        pdialog = new ProgressDialog(this);
        pdialog.setMessage("Changing Password ...");


        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String old_password=old_pass.getText().toString();
                final String new_password=new_pass1.getText().toString();
                String new_password_again=new_pass2.getText().toString();

                if(!(old_password.length()>0) || !(new_password.length()>0) || !(new_password_again.length()>0)  ){

                    Toast.makeText(ChangePassword.this,"Fill all the fields.",Toast.LENGTH_SHORT).show();

                }else{

                    if(!new_password.equals(new_password_again)){

                        Toast.makeText(ChangePassword.this,"New password must match.",Toast.LENGTH_SHORT).show();

                    }else{

                        pdialog.show();

                        ParseUser user=ParseUser.getCurrentUser();
                        String username=user.getUsername();
                        ParseUser.logInInBackground(username, old_password,new LogInCallback() {

                            @Override
                            public void done(ParseUser user, ParseException e) {
                                // TODO Auto-generated method stub

                                if(e == null && user != null){

                                    user.setPassword(new_password);
                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (null == e) {
                                                // report about success

                                                pdialog.dismiss();
                                                Toast.makeText(ChangePassword.this,"Password changed !",Toast.LENGTH_SHORT).show();
                                                finish();

                                            } else {
                                                // report about error

                                                pdialog.dismiss();
                                                Toast.makeText(ChangePassword.this,"Can't change password right now.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else{
                                    pdialog.dismiss();
                                    Toast.makeText(ChangePassword.this,"Wrong old password.",Toast.LENGTH_SHORT).show();
                                }

                            }

                        });


                    }
                }
            }
        });


    }


}






