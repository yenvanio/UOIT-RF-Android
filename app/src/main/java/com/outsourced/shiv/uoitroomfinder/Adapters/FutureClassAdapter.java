package com.outsourced.shiv.uoitroomfinder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.outsourced.shiv.uoitroomfinder.Models.Class;
import com.outsourced.shiv.uoitroomfinder.R;

import java.util.List;

public class FutureClassAdapter extends RecyclerView.Adapter<FutureClassAdapter.MyViewHolder> {

    private List<Class> classList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView code, course, duration;

        public MyViewHolder(View view) {
            super(view);
            code = (TextView) view.findViewById(R.id.code);
            course = (TextView) view.findViewById(R.id.course);
            duration = (TextView) view.findViewById(R.id.duration);
        }
    }


    public FutureClassAdapter(List<Class> classList) {
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_row_future_class, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Class aClass = classList.get(position);
        holder.code.setText(aClass.getCode());
        holder.course.setText(aClass.getCourse());
        holder.duration.setText(aClass.getDuration());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}