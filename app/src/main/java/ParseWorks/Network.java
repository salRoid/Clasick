package ParseWorks;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import Data.SetterGetterUploads;
import Fragments.DashboardFragment;

public class Network {

    public static void retreiveFiles(final Context context) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Upload");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                final List<SetterGetterUploads> setterGetterUploadsArrayList = new ArrayList<SetterGetterUploads>();
                SetterGetterUploads setterGetterUploads = null;

                if (e == null && parseObjects.size() > 0) {


                    ParseObject.pinAllInBackground(parseObjects);

                    for (int i = 0; i < parseObjects.size(); i++) {
                        setterGetterUploads=new SetterGetterUploads();
                        String uploader= parseObjects.get(i).getString("User");
                        String topic=parseObjects.get(i).getString("title");
                        String object_id = parseObjects.get(i).getObjectId();
                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                        String date=times[1]+" "+times[2]+"  "+times[3];

                        setterGetterUploads.setUploader(uploader);
                        setterGetterUploads.setDate(date);
                        setterGetterUploads.setObjectId(object_id);
                        setterGetterUploads.setTopic(topic);


                        setterGetterUploadsArrayList.add(setterGetterUploads);
                        setterGetterUploads=null;


                        DashboardFragment.retrieveFilesCallback(setterGetterUploadsArrayList, true);

                    }

                    }
                else {
                    //faild to download
                    DashboardFragment.retrieveFilesCallback(setterGetterUploadsArrayList, false);

                }
            }
        });

    }


    public static void retreiveFilesTeachers(final Context context) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Upload");
        query.orderByDescending("createdAt");
        query.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                final List<SetterGetterUploads> setterGetterUploadsArrayList = new ArrayList<SetterGetterUploads>();
                SetterGetterUploads setterGetterUploads = null;

                if (e == null && parseObjects.size() > 0) {


                    ParseObject.pinAllInBackground(parseObjects);

                    for (int i = 0; i < parseObjects.size(); i++) {
                        setterGetterUploads=new SetterGetterUploads();
                        String uploader= parseObjects.get(i).getString("User");
                        String topic=parseObjects.get(i).getString("title");
                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                        String date=times[1]+" "+times[2]+"  "+times[3];

                        setterGetterUploads.setUploader(uploader);
                        setterGetterUploads.setDate(date);
                        setterGetterUploads.setTopic(topic);


                        setterGetterUploadsArrayList.add(setterGetterUploads);
                        setterGetterUploads=null;


                        DashboardFragment.retreiveFilesTeachersCallback(setterGetterUploadsArrayList, true);

                    }

                }
                else {
                    //faild to download
                    DashboardFragment.retreiveFilesTeachersCallback(setterGetterUploadsArrayList,false);

                }
            }
        });

    }

    public static void refreshUploads(final Context context) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Upload");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                final List<SetterGetterUploads> setterGetterUploadsArrayList = new ArrayList<SetterGetterUploads>();
                SetterGetterUploads setterGetterUploads = null;

                if (e == null && parseObjects.size() > 0) {


                    ParseObject.pinAllInBackground(parseObjects);

                    for (int i = 0; i < parseObjects.size(); i++) {
                        setterGetterUploads=new SetterGetterUploads();
                        String uploader= parseObjects.get(i).getString("User");
                        String topic=parseObjects.get(i).getString("title");
                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                        String date=times[1]+" "+times[2]+"  "+times[3];

                        setterGetterUploads.setUploader(uploader);
                        setterGetterUploads.setDate(date);
                        setterGetterUploads.setTopic(topic);


                        setterGetterUploadsArrayList.add(setterGetterUploads);
                        setterGetterUploads=null;


                        DashboardFragment.refreshUploadsCallback(setterGetterUploadsArrayList, true);

                    }

                }
                else {
                    //faild to download
                    DashboardFragment.refreshUploadsCallback(setterGetterUploadsArrayList, false);

                }
            }
        });


    }

   /*
 //  LOCALLY

   public static void retreiveFilesLocally(final Context context) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Upload");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                final List<SetterGetterUploads> setterGetterUploadsArrayList = new ArrayList<SetterGetterUploads>();
                SetterGetterUploads setterGetterUploads = null;

                if (e == null && parseObjects.size() > 0) {


                    ParseObject.pinAllInBackground(parseObjects);

                    for (int i = 0; i < parseObjects.size(); i++) {
                        setterGetterUploads=new SetterGetterUploads();
                        String uploader= parseObjects.get(i).getString("User");
                        String topic=parseObjects.get(i).getString("title");
                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                        String date=times[1]+" "+times[2]+"  "+times[3];

                        setterGetterUploads.setUploader(uploader);
                        setterGetterUploads.setDate(date);
                        setterGetterUploads.setTopic(topic);


                        setterGetterUploadsArrayList.add(setterGetterUploads);
                        setterGetterUploads=null;


                        DashboardFragment.retrieveFilesCallback(setterGetterUploadsArrayList, true);

                    }

                }
                else {
                    //faild to download
                    DashboardFragment.retrieveFilesCallback(setterGetterUploadsArrayList, false);

                }
            }
        });

    }*/

   /* public static void retreiveFilesTeachersLocally(final Context context) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Upload");
        query.fromLocalDatastore();
        query.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                final List<SetterGetterUploads> setterGetterUploadsArrayList = new ArrayList<SetterGetterUploads>();
                SetterGetterUploads setterGetterUploads = null;

                if (e == null && parseObjects.size() > 0) {


                    ParseObject.pinAllInBackground(parseObjects);

                    for (int i = 0; i < parseObjects.size(); i++) {
                        setterGetterUploads=new SetterGetterUploads();
                        String uploader= parseObjects.get(i).getString("User");
                        String topic=parseObjects.get(i).getString("title");
                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                        String date=times[1]+" "+times[2]+"  "+times[3];

                        setterGetterUploads.setUploader(uploader);
                        setterGetterUploads.setDate(date);
                        setterGetterUploads.setTopic(topic);


                        setterGetterUploadsArrayList.add(setterGetterUploads);
                        setterGetterUploads=null;


                        DashboardFragment.retreiveFilesTeachersCallback(setterGetterUploadsArrayList, true);

                    }

                }
                else {
                    //failed to download
                    DashboardFragment.retreiveFilesTeachersCallback(setterGetterUploadsArrayList,false);

                }
            }
        });

    }*/
}
