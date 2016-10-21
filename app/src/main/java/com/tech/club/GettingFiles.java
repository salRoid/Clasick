package com.tech.club;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;


public class GettingFiles extends ActionBarActivity {

    final int PICKFILE_RESULT_CODE = 34;
    Button bt_choose, bt_upload;
    EditText ETtitle;
    String title="";
    byte[] file_to_be_uploaded;
    LinearLayout about_file;
    ImageView iv;
    TextView tv;
    String filename = " ";
    String format = " ";
    private ProgressDialog pDialog;

    public static byte[] loadFile(String sourcePath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourcePath);
            return readFully(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static byte[] readFully(InputStream stream) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ETtitle= (EditText) findViewById(R.id.title);

        bt_choose = (Button) findViewById(R.id.pick);
        about_file= (LinearLayout) findViewById(R.id.about_file);
        iv=(ImageView)findViewById(R.id.about_image);
        tv=(TextView)findViewById(R.id.about_text);

        bt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new MaterialFilePicker()
                        .withActivity(GettingFiles.this)
                        .withRequestCode(PICKFILE_RESULT_CODE)
                        .withFilter(Pattern.compile(".*"))
                        // Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .start();


             /*   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);*/

            }
        });


        bt_upload = (Button) findViewById(R.id.upload);
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=ETtitle.getText().toString();


                if(file_to_be_uploaded!=null){
                    if((title.length()>0))
                    {
                        uploadfile();

                    }
                    else{
                        Toast.makeText(GettingFiles.this,"Give a title to the upload.",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(GettingFiles.this,"Please choose a file to upload",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void uploadfile() {

        SharedPreferences sp = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        String upload_by =  ParseUser.getCurrentUser().getUsername().toString();


        Drawable drawable = getResources().getDrawable(
                R.drawable.ic_launcher);
        BitmapDrawable bdrawable = (BitmapDrawable) drawable;
        Bitmap icon = bdrawable.getBitmap();

        Intent notify = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notify,
                PendingIntent.FLAG_UPDATE_CURRENT);



        final int id = 1;
        final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                GettingFiles.this);
        mBuilder.setContentTitle("Clasick")
                .setContentText("File upload in progress.").setLargeIcon(icon)
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_stat_extra);
        mBuilder.setOngoing(true);



        ParseFile file = new ParseFile(filename, file_to_be_uploaded);
        file.saveInBackground(new ProgressCallback() {
            @Override
            public void done(Integer integer) {

                mBuilder.setProgress(100, integer * 5, false);
                mNotifyManager.notify(id, mBuilder.build());

            }
        });


        ParseObject testobject = new ParseObject("Upload");
        testobject.put("data", file);
        testobject.put("title", title);
        testobject.put("User", upload_by);
        testobject.put("fileName",filename);


        testobject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {


                if (e == null) {

                    mBuilder.setContentText("Upload complete")
                            .setProgress(0, 0, false);
                    mBuilder.setOngoing(false);
                    mBuilder.setAutoCancel(true);
                    mNotifyManager.notify(id, mBuilder.build());
                    Toast.makeText(GettingFiles.this, "Uploading completed.", Toast.LENGTH_SHORT).show();


                } else {
                    mBuilder.setContentText("Failed to upload.")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mBuilder.setOngoing(false);
                    mBuilder.setAutoCancel(true);
                    mNotifyManager.notify(id, mBuilder.build());
                    Toast.makeText(GettingFiles.this, "Uploading failed.", Toast.LENGTH_SHORT).show();
                }


            }

        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
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

        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == PICKFILE_RESULT_CODE) {


            if (resultCode == RESULT_OK) {

                String file_path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                format = file_path.substring(file_path.lastIndexOf(".")+1);
                filename=file_path.substring(file_path.lastIndexOf("/")+1);

                if(filename.length()>0)
                    tv.setText(filename);



                if(format!=null && format.contains("pdf")){

                    iv.setImageResource(R.drawable.pdf);


                    try {


                        file_to_be_uploaded = loadFile(file_path);
                        about_file.setVisibility(View.VISIBLE);

                    } catch (IOException e) {

                        e.printStackTrace();
                        Toast.makeText(GettingFiles.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }



                }else  if(format!=null && format.contains("ppt")){

                    iv.setImageResource(R.drawable.ppt);


                    try {


                        file_to_be_uploaded = loadFile(file_path);
                        about_file.setVisibility(View.VISIBLE);

                    } catch (IOException e) {

                        e.printStackTrace();
                        Toast.makeText(GettingFiles.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                }else  if(format!=null && ( format.contains("doc") ||  format.contains("docx")) ){

                    iv.setImageResource(R.drawable.doc);

                    try {


                        file_to_be_uploaded = loadFile(file_path);
                        about_file.setVisibility(View.VISIBLE);

                    } catch (IOException e) {

                        e.printStackTrace();
                        Toast.makeText(GettingFiles.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(this,"Not a valid format.",Toast.LENGTH_SHORT).show();
                }




            }
        }
    }

}
