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
        this.mData = sortData(data);
        this.context = context;
    }
    // Sort array by Time To Branch
    private List<BranchInfo> sortData(List<BranchInfo> data) {
        List<BranchInfo> sortedArray = data;
        BranchInfo temp;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 1; j < (data.size() - i); j++) {

                if (sortedArray.get(j-1).getTimeSeconds() > sortedArray.get(j).getTimeSeconds()) {
                    temp = sortedArray.get(j-1);
                    sortedArray.set(j-1,sortedArray.get(j));
                    sortedArray.set(j, temp);
                }

            }
        }
        return sortedArray;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.branch_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String branch = mData.get(position).getName();
        String time = mData.get(position).getTimeString();
        String distance = mData.get(position).getDistanceText();
        int queue = mData.get(position).getQueueLength();

        holder.myBranchName.setText(branch);
        holder.myTime.setText(time);
        holder.myTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_car, 0, 0, 0);
        holder.myDistance.setText(distance);
        holder.myDistance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_distance, 0, 0, 0);
        holder.myQueue.setText(queue + "");
        holder.myQueue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_people, 0, 0, 0);
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
        public TextView myQueue;

        public ViewHolder(View itemView) {
            super(itemView);
            myBranchName = (TextView) itemView.findViewById(R.id.tvBranchName);
            myDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            myTime  = (TextView) itemView.findViewById(R.id.tvTime);
            myQueue = (TextView) itemView.findViewById(R.id.tvQueue);

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