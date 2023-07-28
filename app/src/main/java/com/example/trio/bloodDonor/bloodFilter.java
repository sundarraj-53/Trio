package com.example.trio.bloodDonor;

import android.widget.Filter;

import java.util.ArrayList;

public class bloodFilter extends Filter {
    private ArrayList<blood> filterList;
    bloodAdapter adapter;
    public bloodFilter(ArrayList<blood> filterList, bloodAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null && constraint.length()>0){
            constraint=constraint.toString().toUpperCase();
            ArrayList<blood> filteredModels=new ArrayList<>();
            for (blood item : filterList) {
                if (item.getBloodgroup().toUpperCase().contains(constraint) ||
                        item.getName().toUpperCase().contains(constraint) ||
                        item.getDepartment().toUpperCase().contains(constraint)) {
                    filteredModels.add(item);
                }
            }
            results.count=filteredModels.size();
            results.values=filteredModels;
        }
        else{
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.arrayList=(ArrayList<blood>)results.values;
        adapter.notifyDataSetChanged();
    }
}
