package Fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.tech.club.GettingFiles;
import com.tech.club.MainActivity;
import com.tech.club.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import Adapters.UploadsListAdapter;
import Data.SetterGetterUploads;
import ParseWorks.Network;

public class DashboardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    static Context context;
    static UploadsListAdapter adapter;
    static List<SetterGetterUploads> sgUploadsArrayList = null;
    static ListView listView, n;
    static ProgressBar spinner;
    static TextView first;
    static ImageView image;
    private static SwipeRefreshLayout swipeLayout;
    RelativeLayout Rstudent, Rteacher;
    private String user_desig;
    private FloatingActionButton fab_upload;

    public static void retrieveFilesCallback(List<SetterGetterUploads> setterGetterUploadsArrayList, boolean success) {
        if (success) {

            sgUploadsArrayList = setterGetterUploadsArrayList;
            adapter = new UploadsListAdapter(context, setterGetterUploadsArrayList);
            listView.setAdapter(adapter);
            spinner.setVisibility(View.INVISIBLE);

        } else {
            image.setVisibility(View.VISIBLE);
            first.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);

        }
    }


/*
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (savedInstanceState == null) {


        } else {
            *//*Network.retreiveFilesLocally(context);*//*
            Network.retreiveFilesTeachersLocally(context);
        }

    }*/

    public static void retreiveFilesTeachersCallback(List<SetterGetterUploads> setterGetterUploadsArrayList, boolean success) {
        if (success) {
            sgUploadsArrayList = setterGetterUploadsArrayList;
            adapter = new UploadsListAdapter(context, setterGetterUploadsArrayList);
            n.setAdapter(adapter);
            spinner.setVisibility(View.INVISIBLE);
        } else {

            image.setVisibility(View.VISIBLE);
            first.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }
    }

    public static void refreshUploadsCallback(List<SetterGetterUploads> setterGetterUploadsArrayList, boolean success) {
        sgUploadsArrayList.clear();
        sgUploadsArrayList.addAll(setterGetterUploadsArrayList);
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    public static void startDownload(String objectId) {



        Drawable drawable = context.getResources().getDrawable(
                R.drawable.ic_launcher);
        BitmapDrawable bdrawable = (BitmapDrawable) drawable;
        Bitmap icon = bdrawable.getBitmap();


        Intent notify = new Intent(context,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, notify,
                PendingIntent.FLAG_UPDATE_CURRENT);



        final int id = 1;
        final NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);

        mBuilder.setContentTitle("Clasick")
                .setContentText("File download in progress.").setLargeIcon(icon)
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_stat_extra);
                   mBuilder.setOngoing(true);







        ParseQuery<ParseObject> query = ParseQuery.getQuery("Upload");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {


                            final String file_name=object.getString("fileName");

                            Log.d("test", "Retrieved the object.");
                            ParseFile fileObject = (ParseFile)object.get("data");
                            fileObject.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Log.d("test", "We've got data in data.");
                                        // use data for something



                                        saveFileToDevice(data,file_name);

                                        mBuilder.setContentText("Download complete")
                                                .setProgress(0, 0, false);
                                        mBuilder.setAutoCancel(true);
                                        mBuilder.setOngoing(false);

                                        mNotifyManager.notify(id, mBuilder.build());
                                        Toast.makeText(context, "Downloading completed.", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Log.d("test", "There was a problem downloading the data.");


                                        mBuilder.setContentText("Download failed.")
                                                .setProgress(0, 0, false);
                                        mBuilder.setOngoing(false);
                                        mBuilder.setAutoCancel(true);
                                        mNotifyManager.notify(id, mBuilder.build());
                                        Toast.makeText(context, "Downloading failed.", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            },new ProgressCallback() {
                                @Override
                                public void done(Integer integer) {

                                    mBuilder.setProgress(100, integer * 5, false);
                                    mNotifyManager.notify(id, mBuilder.build());
                                }
                            });

                        } else {
                            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });







    }

    public static void saveFileToDevice(byte[] data,String file_name) {

        File file;
        FileOutputStream outputStream;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"downloaded_"+file_name);

            outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        context = getActivity();
        first = (TextView) view.findViewById(R.id.first_upload);
        image = (ImageView) view.findViewById(R.id.upload_img);
        spinner = (ProgressBar) view.findViewById(R.id.bar);
        spinner.setVisibility(View.VISIBLE);

        Rstudent = (RelativeLayout) view.findViewById(R.id.student);
        Rteacher = (RelativeLayout) view.findViewById(R.id.teacher);
        fab_upload = (FloatingActionButton) view.findViewById(R.id.upload_file_fab);
        fab_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_file();
            }
        });


        SharedPreferences sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);

        user_desig = sp.getString("who", " ");


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light);
        ;


        if (user_desig.contains("Student")) {
            Rstudent.setVisibility(View.VISIBLE);


            listView = (ListView) view.findViewById(R.id.uploads);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //startDownload(sgUploadsArrayList.get(position).getObjectId());

                }
            });

            Network.retreiveFiles(context);
        }


        if (user_desig.contains("Teacher")) {

            Rteacher.setVisibility(View.VISIBLE);

            n = (ListView) view.findViewById(R.id.your_uploads);
            Network.retreiveFilesTeachers(context);
        }


        return view;

    }

    private void upload_file() {
        startActivity(new Intent(getActivity(), GettingFiles.class));
    }

    @Override
    public void onRefresh() {
        Network.refreshUploads(getActivity());

    }


}
