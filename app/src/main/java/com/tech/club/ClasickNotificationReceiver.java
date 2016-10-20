package com.tech.club;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ClasickNotificationReceiver extends ParsePushBroadcastReceiver {


    private String profileUrl="http://www.webianks.com";
    private String type;
    private String user;
    private String question_id;
    private Uri uri;
    private Context con;
    int mNotificationId = 001;
    private Intent resultIntent;
    String message;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action=intent.getAction();

        Log.d("tag","abc test");

        try {

            JSONObject json =new JSONObject(intent.getExtras().getString("com.parse.Data"));
            profileUrl=json.getString("profileUrl");
            type = json.getString("type");
            message =json.getString("alert");

            if (type.equals("question")){
                user=json.getString("user");
            }

            if(type.equals("Answer")){
                user=json.getString("user");
                question_id=json.getString("question_id");
            }

            if(type.equals("file")){
                user=json.getString("user");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        con=context;

        if (profileUrl == null || profileUrl.contains("webianks.com")) {

            //send notification without profile pic with that alert and title.
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Drawable drawable = context.getResources().getDrawable(
                    R.drawable.clasick);
            BitmapDrawable bdrawable = (BitmapDrawable) drawable;
            Bitmap icon = bdrawable.getBitmap();

            mBuilder = new NotificationCompat.Builder(con);
            mBuilder.setLargeIcon(icon);
            mBuilder.setSmallIcon(R.drawable.clasick);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            mBuilder.setContentTitle("Clasick");
            mBuilder.setContentText(message);
            mBuilder.setAutoCancel(true);
            mBuilder.setSound(uri);
            mBuilder.setPriority(Notification.PRIORITY_MAX);



            // this is the activity that we will send the user, change this to anything you want
            if (type=="question") {

                resultIntent = new Intent(con, Ques.class);
            }
            else{
                resultIntent = new Intent(con, MainActivity.class);
            }

            if (type=="file") {

                resultIntent = new Intent(con, MainActivity.class);
            }
            else{
                resultIntent = new Intent(con, MainActivity.class);
            }


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(con);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0 , PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager) con
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, mBuilder.build());


            Log.d("tag", "Without image notification.");

        } else {
            //send notification with profile pic and with that alert and title.
            new sendNotification(context).execute(profileUrl);

        }



        super.onReceive(context, intent);
    }


    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;

        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;

            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder = new NotificationCompat.Builder(con);
            mBuilder.setLargeIcon(result);
            mBuilder.setSmallIcon(R.drawable.clasick);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            mBuilder.setContentText(message);
            mBuilder.setContentTitle("Clasick");
            mBuilder.setAutoCancel(true);
            mBuilder.setSound(uri);
            mBuilder.setPriority(Notification.PRIORITY_MAX);


            // this is the activity that we will send the user, change this to anything you want
           if (type=="question") {

                resultIntent = new Intent(con, Ques.class);
            }
            else{
                resultIntent = new Intent(con, MainActivity.class);
            }

            if (type=="file") {

                resultIntent = new Intent(con, MainActivity.class);
            }
            else{
                resultIntent = new Intent(con, MainActivity.class);
            }

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(con);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager) con
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(mNotificationId, mBuilder.build());
            Log.d("tag", "With image notification.");

        }
    }




}
