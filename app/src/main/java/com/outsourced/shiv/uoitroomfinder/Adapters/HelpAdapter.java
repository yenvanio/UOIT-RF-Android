package com.outsourced.shiv.uoitroomfinder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.outsourced.shiv.uoitroomfinder.Models.Help;
import com.outsourced.shiv.uoitroomfinder.R;

import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {

    private List<Help> helpList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, body;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            body = (TextView) view.findViewById(R.id.body);
        }
    }


    public HelpAdapter(List<Help> helpList) {
        this.helpList = helpList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Help help = helpList.get(position);
        holder.title.setText(help.getTitle());
        holder.body.setText(help.getBody());
    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }
}