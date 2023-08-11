package com.example.trio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class adminViewAdapter extends RecyclerView.Adapter<adminViewAdapter.ViewHolder>{

    private Context context;
    private ArrayList<AdminHolder> viewList;
    AdminWork adminWork=new AdminWork();
    public adminViewAdapter(Context context, ArrayList<AdminHolder> viewList,AdminWork adminWork) {
        this.context=context;
        this.viewList=viewList;
        this.adminWork=adminWork;
    }

    @NonNull
    @Override
    public adminViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.admin_chance,parent,false);
        return new adminViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adminViewAdapter.ViewHolder holder, int position) {
         AdminHolder uio=viewList.get(position);
        final int[] selectedId = new int[1];
         holder.name.setText(uio.getName());
         holder.department.setText(uio.getDepartment());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, uio.getClubName());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.clubNames.setAdapter(adapter);
        holder.email_value.setText(uio.getEmail());
        holder.clubNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedId[0] =uio.getClubId().get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedId[0]!=0) {
                    try {
                        adminWork.sendData(context, uio.getUserId(), selectedId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,department;
        Spinner clubNames;
        TextView email_text,email_value;
        androidx.appcompat.widget.AppCompatButton submit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_edit);
            department=itemView.findViewById(R.id.user_dept);
            clubNames=itemView.findViewById(R.id.clubSelection);
            submit=itemView.findViewById(R.id.accept);
            email_text=itemView.findViewById(R.id.email_dept_Text);
            email_value=itemView.findViewById(R.id.emailid_dept_Text);
        }
    }
}
