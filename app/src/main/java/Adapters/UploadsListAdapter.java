package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tech.club.R;

import java.util.List;

import Data.SetterGetterUploads;
import Fragments.DashboardFragment;


public class UploadsListAdapter extends BaseAdapter {
    static TextView uploader;
    static TextView date;
    static Button download_button;
    List<SetterGetterUploads> setterGetterUploadsList = null;
    Context context;
    private String user_desig;
    private String uploadTime;
    private String uploadUser;


    public UploadsListAdapter(Context context, List<SetterGetterUploads> setterGetterUploadsList) {

        this.context = context;
        this.setterGetterUploadsList = setterGetterUploadsList;

    }

    @Override
    public int getCount() {
        return setterGetterUploadsList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dashboard_uploads, parent, false);

        } else {

            view = convertView;

        }
        TextView tvTopic= (TextView) view.findViewById(R.id.tv_topic);
        tvTopic.setText(setterGetterUploadsList.get(position).getTopic());


        uploader=(TextView) view.findViewById(R.id.display_uploader);
        date=(TextView)view.findViewById(R.id.display_date);
        download_button= (Button) view.findViewById(R.id.download_button);


        SharedPreferences sp = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE);

        user_desig = sp.getString("who", " ");

        if (user_desig.contains("Student")) {
            download_button.setVisibility(View.VISIBLE);

        }
        if (user_desig.contains("Teacher")) {
            download_button.setVisibility(View.INVISIBLE);
        }




        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DashboardFragment.startDownload(setterGetterUploadsList.get(position).getObjectId());

            }
        });



        uploadUser= setterGetterUploadsList.get(position).getUploader();
        uploadTime=setterGetterUploadsList.get(position).getDate();



        uploader.setText(uploadUser);
        date.setText(uploadTime);
        return view;
    }
}




