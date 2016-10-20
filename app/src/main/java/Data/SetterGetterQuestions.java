package Data;

public class SetterGetterQuestions {

    String question, user, object_id,time,count,ImageUrl;



    public void setQuestion(String question) {
        this.question = question;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public void setTime(String time) {this.time=time ;}

    public void answerCount (String count) {this.count=count;}

    public void setImageUrl(String ImageUrl){this.ImageUrl=ImageUrl;}

    public String getQuestion() {
         return question;
    }

    public String getUser() {
        return user;
    }

    public String getObject_id() {
        return object_id;
    }

    public String getTime() {return time;}

    public  String answerCount() {return count ;}

    public String getImageUrl() {return ImageUrl;}
}
