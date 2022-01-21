package com.example.app_dota;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_dota.model.Heroes;

import java.util.ArrayList;
import java.util.List;

public class HeroesListAdapter extends RecyclerView.Adapter<HeroesListAdapter.MyViewHolder> {
    Context context;
    List<Heroes> heroesList;
    Activity mActivity;
    public HeroesListAdapter(Context context){
        this.context = context;
        heroesList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.heroe_list_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (heroesList != null && position < heroesList.size()) {
            Heroes heroes = heroesList.get(position);
            holder.heroe_name.setText(heroes.getLocalized_name());
            //holder.heroes_image.setImageURI(Uri.parse("https://api.opendota.com"+heroes.getImg()));
            //holder.heroes_image.setImageResource("https://api.opendota.com"+heroes.getImg());
            Glide.with(context).load("https://api.opendota.com"+heroes.getImg()).into(holder.heroes_image);
            Log.i("state",heroes.getLocalized_name());
            Log.i("img","https://api.opendota.com"+heroes.getImg());
        }
    }

    @Override
    public int getItemCount() {
        return heroesList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        ImageView heroes_image;
        TextView heroe_name;
        public MyViewHolder(View itemView){
            super(itemView);
            heroes_image = (ImageView) itemView.findViewById(R.id.heroeImage);
            heroe_name = (TextView)itemView.findViewById(R.id.heroeName);


        }
    }

    public void adicionarLista(List<Heroes> listHeroes){
        for (Heroes h:listHeroes ) {
            heroesList.add(h);
        }
        notifyDataSetChanged();
    }


}
