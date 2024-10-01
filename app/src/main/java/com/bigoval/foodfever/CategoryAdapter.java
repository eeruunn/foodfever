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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ExampleViewHolder>  {
    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<CategoryItem> mExampleList;
    public TextView result;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public CategoryAdapter(Context context, ArrayList<CategoryItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        CategoryItem currentItem = mExampleList.get(position);
        String image = currentItem.getimage();
        String name = currentItem.getmname();
        holder.name.setText(name);
        Picasso.get().load(image).fit().into(holder.mImageView);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView name;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.category_image);
            name = itemView.findViewById(R.id.categoryname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }

                    }
                }
            });
        }
    }

}
