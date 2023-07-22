package com.example.trio.bloodDonor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trio.R;

import java.util.ArrayList;

public class bloodAdapter extends RecyclerView.Adapter<bloodAdapter.ViewHolder> implements Filterable {
    public static ArrayList<com.example.trio.bloodDonor.blood> arrayList;
    public static ArrayList<blood> filterList;
    Context context;
    private static bloodFilter blood;
    public bloodAdapter(ArrayList<com.example.trio.bloodDonor.blood> arrayList, ArrayList<com.example.trio.bloodDonor.blood> filterList, Context context) {
        this.arrayList = arrayList;
        this.filterList = filterList;
        this.context = context;
    }







    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.blood_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         blood ble=arrayList.get(position);
        holder.profile.setImageResource(Integer.parseInt(String.valueOf(ble.getProfile())));
        holder.Name_value.setText(ble.getName());
        holder.department_value.setText(ble.getDepartment());
        holder.blood_value.setText(ble.getBloodgroup());
        holder.phoneNo_value.setText(ble.getPhoneno());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(blood==null){
            blood=new bloodFilter(filterList,this);
        }
        return blood;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView blood_value,department_value,phoneNo_value,Name_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            blood_value=itemView.findViewById(R.id.blood_value);
            department_value=itemView.findViewById(R.id.department_value);
            phoneNo_value=itemView.findViewById(R.id.phoneno_value);
            Name_value=itemView.findViewById(R.id.donor_name);
            profile=itemView.findViewById(R.id.user_profile);
        }

    }
}
