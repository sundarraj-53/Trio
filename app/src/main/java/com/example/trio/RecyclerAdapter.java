package com.example.trio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private ArrayList<Trio> arrayList;

    public RecyclerAdapter(ArrayList<Trio> arrayList){
        this.arrayList=arrayList;
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
        holder.captions.setText(trio.getCaptions());
        holder.Club_name.setText(trio.getClubName());
        byte[] decodedImageBytes = Base64.decode(trio.getProfileIcon(), Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
        holder.profileImage.setImageBitmap(decodedBitmap);
        byte[] decodedPostImageBytes = Base64.decode(trio.getPostImage(), Base64.DEFAULT);
        Bitmap decodedPostBitmap = BitmapFactory.decodeByteArray(decodedPostImageBytes, 0, decodedImageBytes.length);
        holder.poster.setImageBitmap(decodedPostBitmap);
        holder.like.setText(trio.getLike());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage,poster;
        TextView captions,Club_name,like;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.idCVAuthor);
            poster=itemView.findViewById(R.id.post);
            captions=itemView.findViewById(R.id.description);
            Club_name=itemView.findViewById(R.id.club_name);
            like=itemView.findViewById(R.id.count);
        }
    }
}
