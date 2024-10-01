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

public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {
    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<card> mExampleList;
    public Adapter(Context context, ArrayList<card> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemClick2(int position);
        void onItemClick3(int position);
        void onItemClick4(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.homecatcards, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        card currentItem = mExampleList.get(position);
        String imageUrl1 = currentItem.getImageUrl1();
        String imageUrl2 = currentItem.getImageUrl2();
        String imageUrl3 = currentItem.getImageUrl3();
        String imageUrl4 = currentItem.getImageUrl4();
        String imagename1 = currentItem.getImagename1();
        String imagename2 = currentItem.getImagename2();
        String imagename3 = currentItem.getImagename3();
        String imagename4 = currentItem.getImagename4();
        String cheading = currentItem.getheading();

        holder.Imagename1.setText(imagename1);
        holder.Imagename2.setText(imagename2);
        holder.Imagename3.setText(imagename3);
        holder.Imagename4.setText(imagename4);
        holder.Cardheading.setText(cheading);
        Picasso.get().load(imageUrl1).fit().centerInside().into(holder.ImageView1);
        Picasso.get().load(imageUrl2).fit().centerInside().into(holder.ImageView2);
        Picasso.get().load(imageUrl3).fit().centerInside().into(holder.ImageView3);
        Picasso.get().load(imageUrl4).fit().centerInside().into(holder.ImageView4);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView ImageView1;
        public ImageView ImageView2;
        public ImageView ImageView3;
        public ImageView ImageView4;
        public TextView Imagename1;
        public TextView Imagename2;
        public TextView Imagename3;
        public TextView Imagename4;
        public TextView Cardheading;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            ImageView1=itemView.findViewById(R.id.img1);
            ImageView2=itemView.findViewById(R.id.img2);
            ImageView3=itemView.findViewById(R.id.img3);
            ImageView4=itemView.findViewById(R.id.img4);
            Imagename1 = itemView.findViewById(R.id.img1name);
            Imagename2 = itemView.findViewById(R.id.img2name);
            Imagename3 = itemView.findViewById(R.id.img3name);
            Imagename4 = itemView.findViewById(R.id.img4name);
            Cardheading = itemView.findViewById(R.id.cardheading);
            ImageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("you clicked image 1");
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }

                    }
                }
            });
            ImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("you clicked image 2");
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick2(position);
                        }

                    }
                }
            });
            ImageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("you clicked image 3");
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick3(position);
                        }

                    }
                }
            });
            ImageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("you clicked image 4");
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick4(position);
                        }

                    }
                }
            });
        }
    }

}
