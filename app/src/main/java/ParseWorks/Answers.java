package ParseWorks;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.tech.club.AnswerShow;
import com.tech.club.Ques;

import java.util.ArrayList;
import java.util.List;

import Data.SetterGetterAnswers;
import Data.SetterGetterQuestions;

public class Answers {

    public static void retreieveAnswers(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Answers");
        query.orderByAscending("createdAt");
        query.whereEqualTo("quesObjId",objectId);

       query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> parseObjects, ParseException e) {
               final List<SetterGetterAnswers> setterGetterAnswersArrayList = new ArrayList<SetterGetterAnswers>();
               SetterGetterAnswers setterGetterAnswers = null;

               if (e == null && parseObjects.size() > 0) {

                   ParseObject.pinAllInBackground(parseObjects);


                   for (int i = 0; i < parseObjects.size(); i++) {
                       setterGetterAnswers = new SetterGetterAnswers();
                       String answer = parseObjects.get(i).getString("User");
                       String answer_user = parseObjects.get(i).getString("answer");
                       String times[] = parseObjects.get(i).getCreatedAt().toString().split(" ");

                       String time[]=times[3].split(":");
                       String answer_time=times[1] + " " + times[2] + "  " + time[0]+":"+time[1];



                       setterGetterAnswers.setAnswer(answer);
                       setterGetterAnswers.setAnswer_user(answer_user);
                       setterGetterAnswers.setAnswer_time(answer_time);


                       setterGetterAnswersArrayList.add(setterGetterAnswers);
                       setterGetterAnswers = null;

                   }

                   AnswerShow.retrieveAnswersCallback(setterGetterAnswersArrayList, true);


               } else {
                   //faild to download
                   AnswerShow.retrieveAnswersCallback(setterGetterAnswersArrayList, false);
               }

           }
       });
    }
}
