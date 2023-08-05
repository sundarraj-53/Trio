package com.example.trio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private ArrayList<Trio> arrayList;
    private Context context;
    private Trio_Home trio_home;

    public RecyclerAdapter(Context context, ArrayList<Trio> arrayList, Trio_Home trio_home){

        this.context= context;
        this.arrayList=arrayList;
        this.trio_home = trio_home;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layout=LayoutInflater.from(parent.getContext());
        View view=layout.inflate(R.layout.post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trio trio=arrayList.get(position);
        if(trio.getFormat().equals("image")){
            holder.poster.setVisibility(View.VISIBLE);
            holder.text_value.setVisibility(View.GONE);
            Glide.with(context)
                    .load(trio.getPostImage())
                    .into(holder.poster);
            holder.poster.getMaxHeight();
            holder.captions.setText(trio.getCaptions());
            holder.Club_name.setText(trio.getClubName());
            holder.tag.setText(trio.getTag());
            holder.like.setText(String.valueOf(trio.getLike()));
        }
        else{
                holder.text_value.setVisibility(View.VISIBLE);
                holder.poster.setVisibility(View.GONE);
                 holder.text_value.setText(trio.getText());
                holder.text_value.getMaxHeight();
                holder.captions.setText(trio.getCaptions());
                holder.Club_name.setText(trio.getClubName());
                holder.tag.setText(trio.getTag());
                holder.like.setText(String.valueOf(trio.getLike()));
        }
        holder.cmnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,comment_activity.class);
                intent.putExtra("id",trio.getId());
                intent.putExtra("Comments",trio.getComment().toString());
                context.startActivity(intent);

            }
        });

        Log.d("mistral",trio.getPostImage());

    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        MaterialCardView view;
        TextView text;
        ImageButton cmnd;
        TextView captions,Club_name,like,tag,text_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.post);
            captions=itemView.findViewById(R.id.description);
            Club_name=itemView.findViewById(R.id.club_name);
            like=itemView.findViewById(R.id.count);
            cmnd=itemView.findViewById(R.id.comment);
            text_value=itemView.findViewById(R.id.text);
            tag=itemView.findViewById(R.id.tag);
            view=itemView.findViewById(R.id.material);
        }
    }
}
