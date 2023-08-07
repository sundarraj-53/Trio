package com.example.trio;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class commentsBoxAdapter extends RecyclerView.Adapter<commentsBoxAdapter.ViewHolder> {


    private ArrayList<comment> comments;
    public commentsBoxAdapter(ArrayList<comment> comments) {
        this.comments=comments;
    }

    @NonNull
    @Override
    public commentsBoxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.comment_list,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new commentsBoxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            comment comment=comments.get(position);
            holder.commenterName.setText(comment.getUsername());
            Log.d("ARRAY",comment.getUsername());
            holder.comment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commenterName,comment;
            public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterName=itemView.findViewById(R.id.commenter_name);
                Log.d("ARRAY","VALUE of COMMENTER NAME");
            comment=itemView.findViewById(R.id.Usercomment);
        }
    }
}
