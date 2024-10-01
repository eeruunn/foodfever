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

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>  {
    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;
    public TextView result;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public ExampleAdapter(Context context, ArrayList<ExampleItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String creatorName = currentItem.getCreator();
        String seller = currentItem.getseller();
        int likeCount = currentItem.getLikeCount();
        int stock = currentItem.getStock();
        holder.mTextViewseller.setText("Sold by "+seller);
        holder.mTextViewCreator.setText(creatorName);
        holder.mTextViewLikes.setText("₹" + likeCount);
        holder.mTextViewStock.setText("In Stock : " + stock);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;
        public TextView mTextViewStock;
        public TextView mTextViewseller;
        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextViewseller=itemView.findViewById(R.id.seller);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_likes);
            mTextViewStock = itemView.findViewById(R.id.stock);


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
