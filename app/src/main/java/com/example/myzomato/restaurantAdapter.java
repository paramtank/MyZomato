package com.example.myzomato;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class restaurantAdapter extends RecyclerView.Adapter<restaurantAdapter.restaurantViewHolder>{
    private Context rcontext;
    private ArrayList<restaurant_item> restaurant_list;

    public restaurantAdapter(Context context,ArrayList<restaurant_item> restaurant_item_list){
        rcontext=context;
        restaurant_list=restaurant_item_list;
    }

    @NonNull
    @Override
    public restaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(rcontext).inflate(R.layout.restaurant_item, parent ,false);
        return new restaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull restaurantViewHolder holder, int position) {
        restaurant_item current_item=restaurant_list.get(position);

        String res_name=current_item.getRes_name();
        String res_locality=current_item.getLocality();
        String res_cusine=current_item.getCusine();
        String res_rating=current_item.getRating();

        holder.adapter_res_name.setText(res_name);
        holder.adapter_rating.setText(res_rating);
        holder.adapter_locality.setText(res_locality);
        holder.adapter_cusine.setText(res_cusine);
    }

    @Override
    public int getItemCount() {
        return restaurant_list.size();
    }

    public class restaurantViewHolder extends RecyclerView.ViewHolder{
        public TextView adapter_res_name;
        public TextView adapter_locality;
        public TextView adapter_rating;
        public TextView adapter_cusine;
        public restaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_res_name=itemView.findViewById(R.id.resturant_nameid);
            adapter_locality=itemView.findViewById(R.id.localityid);
            adapter_cusine=itemView.findViewById(R.id.cusine_detailsid);
            adapter_rating=itemView.findViewById(R.id.ratingid);
        }
    }

}
