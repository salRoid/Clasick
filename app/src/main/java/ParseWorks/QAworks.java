package ParseWorks;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.tech.club.Ques;

import java.util.ArrayList;
import java.util.List;

import Data.SetterGetterQuestions;

import static com.tech.club.Ques.retrieveQuestionsCallback;

public class QAworks {


    public static void refreshQuestions(final Context context) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ques");
        query.orderByDescending("createdAt");
        query.include("user_profile");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                final List<SetterGetterQuestions> setterGetterQuestionsArrayList=new ArrayList<SetterGetterQuestions>();
                SetterGetterQuestions setterGetterQuestions=null;


                if (e == null && parseObjects.size() > 0) {

                    ParseObject.unpinAllInBackground(parseObjects);
                    ParseObject.pinAllInBackground(parseObjects);


                  //  String question[] = new String[parseObjects.size()];

                    for (int i = 0; i < parseObjects.size(); i++) {
                      //  question[i] = parseObjects.get(i).getString("question");
                        setterGetterQuestions=new SetterGetterQuestions();
                        String question= parseObjects.get(i).getString("question");
                        String  ObjectId = parseObjects.get(i).getObjectId();
                        String user=parseObjects.get(i).getString("User");

                        ParseObject profile_obj=parseObjects.get(i).getParseObject("user_profile");
                        ParseFile file =profile_obj.getParseFile("image");

                        String url="http://www.kybrdbnd.in";

                        if(file !=null)
                         url=file.getUrl();


                        String count=(parseObjects.get(i).getNumber("answer")).toString();

                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                        String time=times[1]+" "+times[2]+"  "+times[3];


                        setterGetterQuestions.setQuestion(question);
                        setterGetterQuestions.setObject_id(ObjectId);
                        setterGetterQuestions.setUser(user);
                        setterGetterQuestions.setTime(time);
                        setterGetterQuestions.setImageUrl(url);
                        setterGetterQuestions.answerCount(count);



                        setterGetterQuestionsArrayList.add(setterGetterQuestions);
                        setterGetterQuestions=null;

                    }
                    Ques.refreshQuestionsCallback(setterGetterQuestionsArrayList);
                   // Toast.makeText(context, " Ques Updated", Toast.LENGTH_LONG).show();
                } else {
                    Ques.refreshQuestionsCallback(null);
                }
            }
        });
    }



    public static void retreiveQuestions(final Context context){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ques");
        query.orderByDescending("createdAt");
        query.include("user_profile");
        query.findInBackground( new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                final List<SetterGetterQuestions> setterGetterQuestionsArrayList=new ArrayList<SetterGetterQuestions>();
                SetterGetterQuestions setterGetterQuestions=null;

                if (e == null && parseObjects.size() > 0) {

                    ParseObject.pinAllInBackground(parseObjects);

                    for (int i = 0; i < parseObjects.size(); i++) {

                       setterGetterQuestions=new SetterGetterQuestions();
                       String question= parseObjects.get(i).getString("question");
                       String  ObjectId = parseObjects.get(i).getObjectId();
                       String user=parseObjects.get(i).getString("User");
                        String count=(parseObjects.get(i).getNumber("answer")).toString();


                        ParseObject profile_obj=parseObjects.get(i).getParseObject("user_profile");
                        ParseFile file =profile_obj.getParseFile("image");

                        String url="http://www.kybrdbnd.in";

                        if(file !=null)
                            url=file.getUrl();


                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                       String time=times[1]+" "+times[2]+"  "+times[3];


                        setterGetterQuestions.setQuestion(question);
                       setterGetterQuestions.setObject_id(ObjectId);
                       setterGetterQuestions.setUser(user);
                        setterGetterQuestions.setImageUrl(url);
                       setterGetterQuestions.setTime(time);
                        setterGetterQuestions.answerCount(count);


                        setterGetterQuestionsArrayList.add(setterGetterQuestions);
                        setterGetterQuestions=null;

                    }


                    retrieveQuestionsCallback(setterGetterQuestionsArrayList, true);

                } else {
                    //faild to download
                    retrieveQuestionsCallback(setterGetterQuestionsArrayList, false);

                }

            }
        });

    }



    public static void retreiveQuestionsLocally(final Context context){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ques");
        query.fromLocalDatastore();
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                final List<SetterGetterQuestions> setterGetterQuestionsArrayList=new ArrayList<SetterGetterQuestions>();
                SetterGetterQuestions setterGetterQuestions=null;

                if (e == null && parseObjects.size() > 0) {


                    ParseObject.pinAllInBackground(parseObjects);

                    for (int i = 0; i < parseObjects.size(); i++) {

                        setterGetterQuestions=new SetterGetterQuestions();
                        String question= parseObjects.get(i).getString("question");
                        String  ObjectId = parseObjects.get(i).getObjectId();
                        String user=parseObjects.get(i).getString("User");
                        String count=(parseObjects.get(i).getNumber("answer")).toString();

                       /* ParseObject profile_obj=parseObjects.get(i).getParseObject("user_profile");
                        ParseFile file =profile_obj.getParseFile("image");

                        String url="http://www.kybrdbnd.in";

                        if(file !=null)
                            url=file.getUrl();
*/



                        String times[]=parseObjects.get(i).getCreatedAt().toString().split(" ");
                        String time=times[1]+" "+times[2]+"  "+times[3];

                        setterGetterQuestions.setQuestion(question);
                        setterGetterQuestions.setObject_id(ObjectId);
                        setterGetterQuestions.setUser(user);
                        setterGetterQuestions.setTime(time);
                        /*setterGetterQuestions.setImageUrl(url);*/
                        setterGetterQuestions.answerCount(count);


                        setterGetterQuestionsArrayList.add(setterGetterQuestions);
                        setterGetterQuestions=null;

                    }


                    Ques.retrieveQuestionsCallback(setterGetterQuestionsArrayList, true);

                } else {
                    //faild to download
                    Ques.retrieveQuestionsCallback(setterGetterQuestionsArrayList, false);
                }

            }
        });

    }



    }

