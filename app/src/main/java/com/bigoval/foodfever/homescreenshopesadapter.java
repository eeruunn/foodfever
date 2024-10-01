package com.bigoval.foodfever;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class homescreenshopesadapter extends RecyclerView.Adapter<homescreenshopesadapter.ExampleViewHolder> {
    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<homescreenshopsitem> mExampleList;
    public homescreenshopesadapter(Context context, ArrayList<homescreenshopsitem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }
    public interface OnItemClickListener{
        void onShopItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.homelayoutshopsitem, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        homescreenshopsitem currentItem = mExampleList.get(position);
        String imageurl = currentItem.getShopImage();
        String shopname = currentItem.getShopname();

        holder.Shopname.setText(shopname);
        Picasso.get().load(imageurl).fit().centerInside().into(holder.ShopImage);

    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView ShopImage;
        public TextView Shopname;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            ShopImage=itemView.findViewById(R.id.shopimageview);
            Shopname = itemView.findViewById(R.id.shopname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onShopItemClick(position);
                        }

                    }
                }
            });
//            ShopImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    System.out.println("you clicked image 1");
//                    if (mListener != null){
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION){
//                            mListener.onItemClick(position);
//                        }
//
//                    }
//                }
//            });

        }
    }

}
