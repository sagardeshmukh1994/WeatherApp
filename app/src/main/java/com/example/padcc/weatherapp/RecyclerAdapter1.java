package com.example.padcc.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter1 extends  RecyclerView.Adapter<RecyclerAdapter1.ViewHolder>  {

    private List<List1> mData;

    private LayoutInflater mInflater;

    Context mContext;

    private ItemClickListener mClickListener;


    public RecyclerAdapter1(Context context, List<List1> weather) {
        mInflater=LayoutInflater.from(context);
        this.mData=weather;
        this.mContext=context;
    }


    @NonNull
    @Override
    public RecyclerAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=mInflater.inflate(R.layout.weatherdatalist,viewGroup,false);

        return new RecyclerAdapter1.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter1.ViewHolder holder, int position) {



        holder.txtid.setText(String.valueOf(mData.get(position).getWeather().get(0).getId()));
        holder.txtmain.setText(String.valueOf(mData.get(position).getWeather().get(0).getMain()));
        holder.txtdesc.setText(String.valueOf(mData.get(position).getWeather().get(0).getDescription()));
        holder.txticon.setText(mData.get(position).getWeather().get(0).getIcon());

        // Picasso.with(mContext).load(mData.get(position).getImageUrl()).fit().into(holder.imgProductImage);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtid,txtmain,txtdesc,txticon;

        ImageView imgProductImage;

        ViewHolder(View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.id);
            txtmain = itemView.findViewById(R.id.main);
            txtdesc = itemView.findViewById(R.id.description);
            txticon = itemView.findViewById(R.id.weathericon);
            // imgProductImage=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}

