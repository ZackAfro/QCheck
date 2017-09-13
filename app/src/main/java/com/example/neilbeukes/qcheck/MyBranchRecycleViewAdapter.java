package com.example.neilbeukes.qcheck;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class MyBranchRecycleViewAdapter extends RecyclerView.Adapter<MyBranchRecycleViewAdapter.ViewHolder>{

    private List<BranchInfo> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public MyBranchRecycleViewAdapter(Context context, List<BranchInfo> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.branch_card, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String branch = mData.get(position).getName();
        String status = mData.get(position).getStatus();
        String time = mData.get(position).getTimeString();
        String distance = mData.get(position).getDistanceText();

        switch (status){
            case "high":
                holder.myBranchStatus.setTextColor(ContextCompat.getColor(context, R.color.high));
                break;
            case "medium":
                holder.myBranchStatus.setTextColor(ContextCompat.getColor(context, R.color.medium));
                break;
            case "low":
                holder.myBranchStatus.setTextColor(ContextCompat.getColor(context, R.color.low));
                break;
        }

        holder.myBranchName.setText(branch);
        holder.myBranchStatus.setText(status);
        holder.myTime.setText(time);
        holder.myTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_car, 0, 0, 0);
        holder.myDistance.setText(distance);
        holder.myDistance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_distance, 0, 0, 0);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myBranchName;
        public TextView myBranchStatus;
        public TextView myTime;
        public TextView myDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            myBranchName = (TextView) itemView.findViewById(R.id.tvBranchName);
            myBranchStatus = (TextView) itemView.findViewById(R.id.tvBranchStatus);
            myDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            myTime  = (TextView) itemView.findViewById(R.id.tvTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    public BranchInfo getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}