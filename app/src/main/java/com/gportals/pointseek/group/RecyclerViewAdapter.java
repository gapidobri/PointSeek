package com.gportals.pointseek.group;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gportals.pointseek.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mPointNames;
    private ArrayList<String> mPointDescriptions;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mPointNames, ArrayList<String> mPointDescriptions, Context mContext) {
        this.mPointNames = mPointNames;
        this.mPointDescriptions = mPointDescriptions;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listpoint, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.tvPointName.setText(mPointNames.get(position));
        holder.tvPointDescription.setText(mPointDescriptions.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mPointNames.get(position));

                Toast.makeText(mContext, mPointNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPointNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPointName, tvPointDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPointName = itemView.findViewById(R.id.tvPointName);
            tvPointDescription = itemView.findViewById(R.id.tvPointDescription);
        }
    }
}
