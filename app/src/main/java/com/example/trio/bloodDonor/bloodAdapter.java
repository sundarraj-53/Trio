package com.example.trio.bloodDonor;

import android.content.Context;
import android.util.Log;
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
    public static ArrayList<blood> arrayList;
    public static ArrayList<blood> filterList;
    public Context context;
    private bloodFilter blood;
    public bloodAdapter(ArrayList<blood> arrayList, ArrayList<blood> filterList, Context context) {
        this.arrayList = arrayList;
        this.filterList = filterList;
        this.context = context;
    }
    public bloodAdapter(ArrayList<blood> arrayList){
        this.arrayList=arrayList;
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
        Log.d("SUNDAR", String.valueOf(Integer.parseInt(String.valueOf(ble.getProfile()))));
        holder.profile.setImageResource(Integer.parseInt(String.valueOf(ble.getProfile())));
        Log.d("SUNDAR",ble.getName());
        holder.Name_value.setText(ble.getName());
        Log.d("SUNDAR",ble.getDepartment());
        holder.department_value.setText(ble.getDepartment());
        Log.d("SUNDAR",ble.getBloodgroup());
        holder.blood_value.setText(ble.getBloodgroup());
        Log.d("SUNDAR",ble.getPhoneno());
        holder.phoneNo_value.setText(ble.getPhoneno());
    }

    @Override
    public int getItemCount() {

        return arrayList != null ?arrayList.size():0;
    }

    @Override
    public Filter getFilter() {
        if(blood==null){
            blood=new bloodFilter(filterList,this);
        }
        return blood;
    }

class ViewHolder extends RecyclerView.ViewHolder{

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
