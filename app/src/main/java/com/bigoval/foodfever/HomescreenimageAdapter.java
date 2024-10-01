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

public class HomescreenimageAdapter extends RecyclerView.Adapter<HomescreenimageAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<HomescreenimageItem> mExampleList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void Onimageitemclick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public HomescreenimageAdapter(Context context, ArrayList<HomescreenimageItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.homeimageitem, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        HomescreenimageItem currentItem = mExampleList.get(position);
        String imageUrl1 = currentItem.getImageUrl();
        String imagename1 = currentItem.getname();
        Picasso.get().load(imageUrl1).fit().centerInside().into(holder.ImageView1);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView ImageView1;
        public TextView Imagename1;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            ImageView1=itemView.findViewById(R.id.homescreenimage);

            ImageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.Onimageitemclick(position);
                        }

                    }
                }
            });

        }
    }

}
