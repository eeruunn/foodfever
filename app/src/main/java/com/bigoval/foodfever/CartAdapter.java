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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ExampleViewHolder>  {
//    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<CartItem> mExampleList;
    public TextView result;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteItemClick(int position);
        void onSubQtyClick(int position);
        void onAddQtyClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public CartAdapter(Context context, ArrayList<CartItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        CartItem currentItem = mExampleList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String name = currentItem.getname();
        String seller = currentItem.getseller();
        String ctext= currentItem.getctext();
        int price = currentItem.getprice();
        int quantity = currentItem.getquantity();
        holder.mseller.setText("Sold by "+seller);
        holder.mname.setText(name);
        holder.mprice.setText("₹" + price);
        holder.mqty.setText(""+quantity);
        holder.mctext.setText(ctext);
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
        public TextView mctext;
        public TextView mqty;
        public ImageView dltbtn;
        public Button addbtn;
        public Button subbtn;
        public ExampleViewHolder(View itemView) {
            super(itemView);

            mseller=itemView.findViewById(R.id.cartseller);
            mImageView = itemView.findViewById(R.id.cart_image_view);
            mname = itemView.findViewById(R.id.cartname);
            mprice = itemView.findViewById(R.id.cartprice);
            mqty = itemView.findViewById(R.id.cartqty);
            dltbtn = itemView.findViewById(R.id.cartdlt);
            addbtn = itemView.findViewById(R.id.cartadd);
            subbtn = itemView.findViewById(R.id.cartsub);
            mctext=itemView.findViewById(R.id.ctext);

            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onAddQtyClick(position);
                        }

                    }
                }
            });

            subbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onSubQtyClick(position);
                        }

                    }
                }
            });
            dltbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onDeleteItemClick(position);
                        }

                    }
                }
            });

        }
    }

}
