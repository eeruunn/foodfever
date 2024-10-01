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

public class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.ExampleViewHolder>  {
    private Context mContext;
    private ArrayList<OrderedItem> mExampleList;
    public TextView result;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void oncancelclick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public OrderedItemAdapter(Context context, ArrayList<OrderedItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ordered_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        OrderedItem currentItem = mExampleList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String name = currentItem.getname();
        String seller = currentItem.getseller();
        String orderedon = currentItem.getorderedon();
        String ordertext = currentItem.gettext();
        String quantity = currentItem.getquantity();
        String status = currentItem.getstatus();
        if(status.equals("cancelled") || status.equals("delivered")){
            holder.ordercancelbtn.setVisibility(View.GONE);
        }
        int price = currentItem.getprice();
        holder.mTextViewprice.setText("₹ "+price);
        holder.mTextViewname.setText(name);
        holder.mTextorderstatus.setText(status);
        holder.mTextorderedon.setText(orderedon);
        holder.mquantity.setText("Qty: "+ quantity);
        holder.mtext.setText(""+ordertext);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewname;
        public TextView mTextViewprice;
        public TextView mTextorderstatus;
        public TextView mTextorderedon;
        public TextView mquantity;
        public TextView mtext;
        Button ordercancelbtn;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.orderedproductimage);
            mTextViewname = itemView.findViewById(R.id.orderedproductname);
            mTextViewprice = itemView.findViewById(R.id.orderedproductprice);
            mTextorderedon = itemView.findViewById(R.id.orderedon);
            mTextorderstatus = itemView.findViewById(R.id.orderstatus);
            mquantity = itemView.findViewById(R.id.orderitemquantity);
            mtext = itemView.findViewById(R.id.orderitemdescription);
            ordercancelbtn=itemView.findViewById(R.id.cancelorder);

            ordercancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.oncancelclick(position);
                        }

                    }
                }
            });

        }
    }

}
