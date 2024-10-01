package com.bigoval.foodfever;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Checkoutadapter extends RecyclerView.Adapter<Checkoutadapter.ExampleViewHolder>  {
//    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<CartItem> mExampleList;
    public TextView result;


    public Checkoutadapter(Context context, ArrayList<CartItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.checkoutitem, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        CartItem currentItem = mExampleList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String name = currentItem.getname();
        String seller = currentItem.getseller();
        int price = currentItem.getprice();
        int quantity = currentItem.getquantity();
        int deliveryt =currentItem.getdeliverablewithin();
        holder.mseller.setText("Sold by "+seller);
        holder.mname.setText(name);
        holder.mprice.setText("₹" + price);
        holder.mqty.setText("qty: "+quantity);
        if( deliveryt == 0){
            holder.mdeliverytime.setText("Same day delivery");

        }
        else{
            holder.mdeliverytime.setText("Delivery within "+deliveryt+" days");
        }
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mname;
        public TextView mprice;
        public TextView mseller;
        public TextView mqty;
        public TextView mdeliverytime;

        public ImageView dltbtn;
        public Button addbtn;
        public Button subbtn;
        public ExampleViewHolder(View itemView) {
            super(itemView);

            mseller=itemView.findViewById(R.id.checkoutseller);
            mImageView = itemView.findViewById(R.id.checkout_image_view);
            mname = itemView.findViewById(R.id.checkoutname);
            mprice = itemView.findViewById(R.id.checkoutprice);
            mqty = itemView.findViewById(R.id.checkoutquantity);
            mdeliverytime = itemView.findViewById(R.id.deliverytime);


        }
    }

}
