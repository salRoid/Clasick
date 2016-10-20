package com.tech.club;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private LayoutInflater inflater;
    Context context;
    private ClickListener clickListener;
    List<Information> data= Collections.emptyList() ;
    public Adapter(Context context ,List<Information> data) {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder =new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
   Information current = data.get(position);
    holder.title.setText(current.title);
    holder.icon.setImageResource(current.iconid);
    }


    public void setClickListener (ClickListener clickListener) {
        this.clickListener=clickListener;

    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.list_text);
            icon = (ImageView) itemView.findViewById(R.id.icon_list);

        }

        @Override
        public void onClick(View v) {
            if (clickListener!=null){
                clickListener.itemClicked(v,getPosition());
            }

        }
    }

    public interface ClickListener {
        public void itemClicked(View v ,int position);


    }
}
