package com.example.trio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class adminAdapter extends RecyclerView.Adapter<adminAdapter.ViewHolder>{
    public  ArrayList<request> arrayList;
    public Context context;
    public admin_Request ad=new admin_Request();
    public adminAdapter(Context context, ArrayList<request> arrayList) {
        this.context=context;
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
            holder.club_name.setText(re.getClub());
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                     Log.d("ASHWIN", String.valueOf(context));
                        ad.sendValue(context,re.dept_id,true,re.getId());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        ad.setContext(ad.getContext());
                        Log.d("ASHWIN", String.valueOf(context));
                        ad.sendValue(context,re.dept_id,false,re.getId());
                        ad.loadData(context);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,department,dept_id,club_name;
        Button accept,reject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.request_name);
            department=itemView.findViewById(R.id.request_depart);
            dept_id=itemView.findViewById(R.id.userId);
            accept=itemView.findViewById(R.id.accept_request);
            reject=itemView.findViewById(R.id.reject_request);
            club_name=itemView.findViewById(R.id.club_name);
        }
    }
}
