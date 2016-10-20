package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tech.club.R;

import java.util.List;

import Data.SetterGetterAnswers;


public class AnswersListAdapter extends BaseAdapter {
    static TextView comment_by;
    static TextView clock;
    List<SetterGetterAnswers> setterGetterAnswersList = null;
    Context context;
    private String answer_Time;
    private String answer_User;




    public AnswersListAdapter(Context context, List<SetterGetterAnswers> setterGetterAnswersList) {

        this.context = context;
        this.setterGetterAnswersList = setterGetterAnswersList;

    }
    @Override
    public int getCount() {
        return setterGetterAnswersList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.answers, parent, false);

        } else {

            view = convertView;

        }
        TextView comment= (TextView) view.findViewById(R.id.display_commenter);
        comment.setText(setterGetterAnswersList.get(position).getAnswer());


        comment_by=(TextView) view.findViewById(R.id.comment);
        clock=(TextView)view.findViewById(R.id.display_second);



        answer_User= setterGetterAnswersList.get(position).getAnswer_user();
        answer_Time=setterGetterAnswersList.get(position).getAnswer_time();



        comment_by.setText(answer_User);
        clock.setText(answer_Time);
        return view;
    }

}
