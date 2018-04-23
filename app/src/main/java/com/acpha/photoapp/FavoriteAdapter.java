package com.acpha.photoapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acpha.photoapp.Models.Photo.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andy on 12/29/2016.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private ArrayList<String> data;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;
        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.fav_text);
            imageView = (ImageView) view.findViewById(R.id.fav_image);
        }
    }
    public FavoriteAdapter(Context context, ArrayList<String> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.favorite_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        //final Photo photo = data.get(position);
        Picasso.with(context).load(data.get(position))
                .resize(150,150)//holder.imageView.getWidth(), holder.imageView.getHeight())
                .centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String getUrl(int position) {
        if(position >= 0 && position < data.size()) {
            return data.get(position);
        }else{
            return null;
        }
    }
}
