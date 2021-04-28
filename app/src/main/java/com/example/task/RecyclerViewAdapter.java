package com.example.task;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task.Data.Models.Datum;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Resources resource;
    Context context;
    List<Datum> list;


    public RecyclerViewAdapter( Context context, List<Datum> list){
        this.list=list;
        this.resource=resource;
        this.context = context;

    }

    public void addDataToList(List<Datum> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.car_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.brand)
        TextView brand;
        @BindView(R.id.construction_year)
        TextView construction_year;
        @BindView(R.id.is_used_Or_new)
        TextView is_used_Or_new;
        @BindView(R.id.car_img)
        ImageView car_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Datum data){
            if(data.getImageUrl()!=null) {
                Picasso.with(context).load(data.getImageUrl()).error(context.getResources().getDrawable(R.drawable.ic_baseline_broken_image_24)).into(car_img);
            }else {
               car_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_broken_image_24));
            }
            brand.setText(data.getBrand());
            if(data.getIsUsed()) {
                is_used_Or_new.setText(context.getResources().getString(R.string.isused));
            }else {
                is_used_Or_new.setText(context.getResources().getString(R.string.isnew));
            }
            construction_year.setText(data.getConstructionYear());

        }



    }
}
