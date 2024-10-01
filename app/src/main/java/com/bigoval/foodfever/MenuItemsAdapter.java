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

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.ExampleViewHolder>  {
    private OnItemClickListener mListener;
    private Context mContext;
    public MenuCatsAdapter.ExampleViewHolder exampleViewHolder;
    private ArrayList<Menuitems> mMenuitems;
    public TextView result;

    public interface OnItemClickListener{
        void onproductclick(int position,int pp);
        void onaddtocartbuttonclick(int position,int pp);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public MenuItemsAdapter(ArrayList<Menuitems> menuitems,MenuCatsAdapter.ExampleViewHolder eXampleViewHolder) {
        mMenuitems = menuitems;
        this.exampleViewHolder = eXampleViewHolder;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        Menuitems currentItem = mMenuitems.get(position);
        String name = currentItem.getItemname();
        String price = currentItem.getItemPrice();
        String imageurl = currentItem.getItemImage();
        holder.mname.setText(name);
        holder.mprice.setText("₹ "+price);
        Picasso.get().load(imageurl).fit().centerInside().into(holder.image);

    }
    @Override
    public int getItemCount() {
        return mMenuitems.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
//        public ImageView mImageView;
        public TextView mname;
        public TextView mprice;
        private ImageView image;
        private Button addtocartbutton;
        public ExampleViewHolder(View itemView) {
            super(itemView);

            mname=itemView.findViewById(R.id.menu_item_itemname);
            mprice = itemView.findViewById(R.id.menu_item_itemprice);
            image=itemView.findViewById(R.id.menu_item_image);
            addtocartbutton = itemView.findViewById(R.id.menu_item_addtocartbutton);


//
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        int pp = exampleViewHolder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onproductclick(position,pp);
                        }

                    }
                }
            });

            addtocartbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        int pp = exampleViewHolder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onaddtocartbuttonclick(position,pp);
                        }

                    }
                }
            });
        }
    }

}
