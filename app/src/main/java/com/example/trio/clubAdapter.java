package com.example.trio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trio.Storage.Storage;

import java.util.ArrayList;

public class clubAdapter extends RecyclerView.Adapter<clubAdapter.ViewHolder>{

    public ArrayList<club> arrayList;
    Storage store=new Storage();
    club_register club_register=new club_register();
    public clubAdapter( ArrayList<club> arrayList) {
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.club_register,parent,false);
        return new clubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        club cl=arrayList.get(position);
        holder.check.setId(cl.getId());
        holder.check.setText(cl.getClubName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox check;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check=itemView.findViewById(R.id.check);
        }
    }
}
