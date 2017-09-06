package com.example.neilbeukes.qcheck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class MyBranchRecycleViewAdapter extends RecyclerView.Adapter<MyBranchRecycleViewAdapter.ViewHolder> {

    private List<BranchInfo> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MyBranchRecycleViewAdapter(Context context, List<BranchInfo> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
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
        String adress = mData.get(position).getAdress();

        holder.myBranchName.setText(branch);
        holder.myBranchStatus.setText(status);
        holder.myBranchAdress.setText(adress);
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
        public TextView myBranchAdress;

        public ViewHolder(View itemView) {
            super(itemView);
            myBranchName = (TextView) itemView.findViewById(R.id.tvBranchName);
            myBranchStatus = (TextView) itemView.findViewById(R.id.tvBranchStatus);
            myBranchAdress = (TextView) itemView.findViewById(R.id.tvBranchAdress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id).getName();
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