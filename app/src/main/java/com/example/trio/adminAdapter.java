package com.example.trio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adminAdapter extends RecyclerView.Adapter<adminAdapter.ViewHolder>{
    public static ArrayList<request> arrayList;
    public adminAdapter(ArrayList<request> arrayList) {
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public adminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.admin_request,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adminAdapter.ViewHolder holder, int position) {
            request re=arrayList.get(position);
            holder.name.setText(re.getName());
            holder.department.setText(re.getDepartment());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,department;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.request_name);
            department=itemView.findViewById(R.id.request_depart);
        }
    }
}
