package com.tech.club;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.QuestionsListAdapter;
import Data.SetterGetterQuestions;
import ParseWorks.QAworks;


public class Ques extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    static ListView questionList;
    private static SwipeRefreshLayout swipeLayout;
    static QuestionsListAdapter adapter;
    static ProgressBar spinner;
    static Context context;
    public static List<SetterGetterQuestions> sgQuestionsArrayList = null;
    String questionClicked, questionsObjectId;
    private String questionsUser;
    static TextView first;
    private FloatingActionButton upload_quesFab;
    public static boolean first_run=true;
    static ImageView first_img;
    private String questionTime;
    RelativeLayout mainContent;
    private ProgressDialog pdialog;
    private String mr_user;
    private DrawerLayout drawerLayout;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ques, container, false);



        context = getActivity();

        mr_user=ParseUser.getCurrentUser().getUsername().toString();
        first = (TextView) view.findViewById(R.id.first_Answer);
        first_img = (ImageView) view.findViewById(R.id.answer_img);
        spinner = (ProgressBar) view.findViewById(R.id.bar);
        spinner.setVisibility(View.VISIBLE);
        mainContent = (RelativeLayout) view.findViewById(R.id.main_content);
        questionList = (ListView) view.findViewById(R.id.ques_list);
        questionList.setOnItemClickListener(this);
        questionList.setOnItemLongClickListener(this);


        SharedPreferences sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);



        upload_quesFab = (FloatingActionButton) view.findViewById(R.id.upload_ques_fab);



        upload_quesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload_ques();
            }
        });


        //Swipe Refresh Layout

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light);
        ;

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // retreive the questions from the parse

        run_once();
    }

    public void run_once(){
        if(first_run){
            QAworks.retreiveQuestions(context);
            first_run=false;
        }

        QAworks.retreiveQuestionsLocally(context);
        QAworks.retreiveQuestions(context);


    }

    public void upload_ques() {

        startActivity(new Intent(getActivity(), QuesSending.class));
    }


    @Override
    public void onRefresh() {

        QAworks.refreshQuestions(getActivity());
    }




    public static void refreshQuestionsCallback(List<SetterGetterQuestions> setterGetterQuestionsArrayList) {

        sgQuestionsArrayList.clear();
        sgQuestionsArrayList.addAll(setterGetterQuestionsArrayList);
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if (sgQuestionsArrayList != null) {
            questionClicked = sgQuestionsArrayList.get(position).getQuestion();
            questionsObjectId = sgQuestionsArrayList.get(position).getObject_id();
            questionsUser = sgQuestionsArrayList.get(position).getUser();
            questionTime = sgQuestionsArrayList.get(position).getTime();
        }


        Intent intent = new Intent(getActivity(), AnswerShow.class);
        intent.putExtra("question", questionClicked);
        intent.putExtra("object_id", questionsObjectId);
        intent.putExtra("user", questionsUser);
        intent.putExtra("time", questionTime);
        startActivity(intent);


    }



    public static void retrieveQuestionsCallback(List<SetterGetterQuestions> setterGetterQuestionsArrayList, boolean success) {

        if(first_run) {


            if (success) {
                sgQuestionsArrayList = setterGetterQuestionsArrayList;
                adapter = new QuestionsListAdapter(context, setterGetterQuestionsArrayList);
                questionList.setAdapter(adapter);
                spinner.setVisibility(View.INVISIBLE);

                // Toast.makeText(Ques.this, " Ques Updated", Toast.LENGTH_SHORT).show();
            } else {

                first_img.setVisibility(View.VISIBLE);
                first.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.INVISIBLE);
            }
        }


        else {
            if (success) {
                sgQuestionsArrayList = setterGetterQuestionsArrayList;
                adapter = new QuestionsListAdapter(context, setterGetterQuestionsArrayList);
                questionList.setAdapter(adapter);
                spinner.setVisibility(View.INVISIBLE);

                // Toast.makeText(Ques.this, " Ques Updated", Toast.LENGTH_SHORT).show();
            } else {

                QAworks.retreiveQuestions(context);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


        if (sgQuestionsArrayList != null) {
            questionsObjectId = sgQuestionsArrayList.get(position).getObject_id();
            questionsUser = sgQuestionsArrayList.get(position).getUser();
        }


        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);


        if (mr_user.contains(questionsUser)) {
            arrayAdapter.add("Delete");
            arrayAdapter.add("Edit");



            adb.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(which==0) {
                        pdialog = new ProgressDialog(getActivity());
                        pdialog.setCancelable(false);
                        pdialog.setMessage("deleting..");
                        pdialog.show();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ques");

                        query.whereEqualTo("objectId", questionsObjectId);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if (e == null) {
                                    for (ParseObject delete : parseObjects) {
                                        delete.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    onRefresh();
                                                    pdialog.dismiss();
                                                    QAworks.refreshQuestions(getActivity());
                                                    Snackbar.make(mainContent, "Deleted", Snackbar.LENGTH_SHORT).show();
                                                } else
                                                    pdialog.dismiss();
                                                Snackbar.make(mainContent, " Not Deleted", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }
                            }
                        });
                    }
                    if (which==1){

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Edit");

                        final EditText input = new EditText(context);
                        FrameLayout container = new FrameLayout(context);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = 30;
                        params.rightMargin = 50;
                        input.setLayoutParams(params);


                        String puttingText =sgQuestionsArrayList.get(position).getQuestion();

                        input.setText(puttingText);
                        input.setTextColor(Color.parseColor("#303030"));
                        input.setSelection(puttingText.length());
                        input.requestFocus();

                        container.addView(input);


                        alertDialog.setView(container);
                        alertDialog.setPositiveButton("Done",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                        mProgressDialog = new ProgressDialog(getActivity());

                                        mProgressDialog.setMessage("Working...");
                                        mProgressDialog.setCancelable(false);
                                        mProgressDialog.show();

                                        String newPuttingText = input.getText().toString();

                                        editQuestion(position, sgQuestionsArrayList.get(position).getObject_id(), newPuttingText);


                                    }
                                });

                        alertDialog.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();


                    }


                }


            });

            adb.show();



        }
        else {
            arrayAdapter.add("View Profile");


            adb.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), Profile.class);
                    intent.putExtra("user", questionsUser);
                    startActivity(intent);

                    Snackbar.make(mainContent, "Update still in progress", Snackbar.LENGTH_SHORT).show();
                }

            });

            adb.show();

        }

        return true;
    }

    private void editQuestion(final int position, String object_id, final String newPuttingText) {

        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("Ques");
        parseQuery.getInBackground(object_id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if (e==null && parseObject!=null) {

                    parseObject.put("question", newPuttingText);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {



                                mProgressDialog.dismiss();
                                sgQuestionsArrayList.get(position).setQuestion(newPuttingText);
                                adapter.notifyDataSetChanged();
                                showSnackBar("Done");

                            } else {
                                //Can't Save

                                mProgressDialog.dismiss();
                                showSnackBar(e.getMessage());
                            }
                        }
                    });

                }else{
                    mProgressDialog.dismiss();
                    showSnackBar(e.getMessage());
                }


            }
        });
    }

    private void showSnackBar(String done) {
        Snackbar.make(mainContent,done,Snackbar.LENGTH_SHORT).show();
    }

}

