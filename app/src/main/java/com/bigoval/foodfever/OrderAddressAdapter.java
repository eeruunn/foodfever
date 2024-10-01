package com.bigoval.foodfever;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAddressAdapter extends RecyclerView.Adapter<OrderAddressAdapter.ExampleViewHolder>  {

    private Context mContext;
    private ArrayList<AddressViewItem> mExampleList;
    public TextView result;
    private OrderAddressAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onSelect(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public OrderAddressAdapter(Context context, ArrayList<AddressViewItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.orderaddressitem, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        AddressViewItem currentItem = mExampleList.get(position);
        String name = currentItem.getname();
        String phone = currentItem.getPhone();
        String pincode = currentItem.getPincode();
        String address = currentItem.getAddress();
        String housename = currentItem.getHousename();
        String townname = currentItem.getTownname();
        String landmark = currentItem.getLandmark();
        holder.mTextViewname.setText(name);
        holder.mTextViewphone.setText(phone);
        holder.mTextViewpincode.setText(pincode);
        holder.mTextViewaddress.setText(address);
        holder.mTextViewtownname.setText(housename);
        holder.mTextViewhousename.setText(townname);
        holder.mTextViewlandmark.setText(landmark);

    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewname;
        public TextView mTextViewpincode;
        public TextView mTextViewphone;
        public TextView mTextViewaddress;
        public TextView mTextViewhousename;
        public TextView mTextViewtownname;
        public TextView mTextViewlandmark;
        public Button mselectbtn;
        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextViewname=itemView.findViewById(R.id.addressviewname);
            mTextViewpincode = itemView.findViewById(R.id.addressviewpincode);
            mTextViewphone = itemView.findViewById(R.id.addressviewphone);
            mTextViewaddress = itemView.findViewById(R.id.addressviewaddress);
            mTextViewhousename = itemView.findViewById(R.id.addressviewhousename);
            mTextViewtownname=itemView.findViewById(R.id.addressviewtown);
            mTextViewlandmark = itemView.findViewById(R.id.addressviewlandmark);
            mselectbtn= itemView.findViewById(R.id.addressselectedbtn);

            mselectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        System.out.println(position);
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onSelect(position);
                        }

                    }
                }
            });
//
//            medit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mListener != null){
//                        int position = getAdapterPosition();
//                        System.out.println(position);
//                        if (position != RecyclerView.NO_POSITION){
//                            mListener.onEditClick(position);
//                        }
//
//                    }
//
//                }
//            });
        }
    }


}
