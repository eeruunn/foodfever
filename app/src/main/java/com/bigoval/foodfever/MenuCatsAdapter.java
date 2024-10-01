package com.bigoval.foodfever;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MenuCatsAdapter extends RecyclerView.Adapter<MenuCatsAdapter.ExampleViewHolder> implements MenuItemsAdapter.OnItemClickListener  {
    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<Menucatsitem> menucatsitems;
    private FragmentManager mfragmentmanager;
    private Fragment mfragment;
//    private MenuItemsAdapter adapter;
    private RecyclerView.RecycledViewPool itemsrecycledViewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<Menucatsitem> mExampleList;
    public TextView result;
    private Activity aCtivity;
    private SharedPreferences Preferences;
    RequestQueue requestQueue;
    String Customerid;
    String Sellerid;
    String checkcarturl = "http://dpend.pythonanywhere.com/products/checkcartseller/";
    String addtocarturl = "http://dpend.pythonanywhere.com/products/addtocart/";
    RequestQueue RQ;
    Boolean sameseller;

    @Override
    public void onproductclick(int position,int pp) {
        Menucatsitem item = mExampleList.get(pp);
        JSONArray menuitems = item.getProductsarray();
        ArrayList<Menuitems> items = new ArrayList<>();
        for(int i=0;i<menuitems.length();i++){
            try {
                JSONObject object = menuitems.getJSONObject(i);
                Menuitems menuitems1 =new Menuitems();
                menuitems1.setItemname(object.getString("name"));
                menuitems1.setItemPrice(object.getString("price"));
                menuitems1.setItemImage(object.getString("image"));
                menuitems1.setItemId(object.getString("id"));
                items.add(menuitems1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("productid",items.get(position).getItemId());
        bundle.putString("type","productfrommenu");
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
//        Toast.makeText(mContext,items.get(position).getItemId(),Toast.LENGTH_SHORT).show();
        mfragmentmanager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onaddtocartbuttonclick(int position, int pp) {
        SharedPreferences sharedPreferences = Preferences;
        boolean loggedin = sharedPreferences.getBoolean("Loggedin",false);
        Customerid = sharedPreferences.getString("Customerid","");
        Menucatsitem item = mExampleList.get(pp);
        JSONArray menuitems = item.getProductsarray();
        ArrayList<Menuitems> items = new ArrayList<>();
        for(int i=0;i<menuitems.length();i++){
            try {
                JSONObject object = menuitems.getJSONObject(i);
                Menuitems menuitems1 =new Menuitems();
                menuitems1.setItemname(object.getString("name"));
                menuitems1.setItemPrice(object.getString("price"));
                menuitems1.setItemImage(object.getString("image"));
                menuitems1.setItemId(object.getString("id"));
                menuitems1.setWriteable(object.getBoolean("writeable"));
                items.add(menuitems1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(loggedin){
            String data = "{" +
                    "\"id\"" + ":" + "\"" + Customerid + "\"" +
                    "}";
            getcartdetails(data,Customerid,items.get(position).getItemId(),items.get(position).getWriteable());
        }
        else {
            Intent intent = new Intent(aCtivity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public MenuCatsAdapter(Context context, ArrayList<Menucatsitem> exampleList, FragmentManager fragmentmanager,Fragment fragment,Activity activity,SharedPreferences preferences,String sellerid) {
        mContext = context;
        mExampleList = exampleList;
        mfragmentmanager = fragmentmanager;
        mfragment = fragment;
        aCtivity = activity;
        Preferences = preferences;
        Sellerid = sellerid;

    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.menu_cat_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Menucatsitem currentItem = mExampleList.get(position);
        String name = currentItem.getCatname();
        JSONArray menuitems = currentItem.getProductsarray();
        ArrayList<Menuitems> items = new ArrayList<>();
        for(int i=0;i<menuitems.length();i++){
            try {
                JSONObject object = menuitems.getJSONObject(i);
                Menuitems menuitems1 =new Menuitems();
                menuitems1.setItemname(object.getString("name"));
                menuitems1.setItemPrice(object.getString("price"));
                menuitems1.setItemImage(object.getString("image"));
                menuitems1.setItemId(object.getString("id"));
                menuitems1.setWriteable(object.getBoolean("writeable"));
                items.add(menuitems1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        MenuItemsAdapter adapter = new MenuItemsAdapter(items,holder);
        holder.itemsrecyclerView.setAdapter(adapter);
        holder.itemsrecyclerView.setHasFixedSize(false);
        holder.itemsrecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemsrecyclerView.getContext()));
//        String creatorName = currentItem.getCreator();
//        String seller = currentItem.getseller();
//        int likeCount = currentItem.getLikeCount();
//        int stock = currentItem.getStock();
//        holder.mTextViewseller.setText("Sold by "+seller);
//        holder.mTextViewCreator.setText(creatorName);
//        holder.mTextViewLikes.setText("₹" + likeCount);
        holder.mCatname.setText(""+name);
        adapter.setOnItemClickListener(this);
//        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mCatname;
        public RecyclerView itemsrecyclerView;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            itemsrecyclerView=itemView.findViewById(R.id.menu_items_recyclerview);
            mCatname=itemView.findViewById(R.id.menu_cat_item_categoryname);



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
    private void Submit(String data) {
        final LoadingDialog pd = new LoadingDialog(aCtivity);
        final String savedata = data;
        String URL = addtocarturl;
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestQueue = Volley.newRequestQueue(aCtivity.getApplicationContext());
        pd.show();
//        pd.setCanceledOnTouchOutside(getRetainInstance());

        pd.setContentView(R.layout.dialog);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String responsecode = objres.getString("responsecode");
                    if (!responsecode.equals("0")) {
                        Toast.makeText(aCtivity.getApplicationContext(), "Sorry not enough stock.Try decreasing quantity", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(aCtivity.getApplicationContext(), "product added to cart", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(aCtivity.getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(aCtivity.getApplicationContext(), "Add to cart failed", Toast.LENGTH_LONG).show();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }

    private void cartctextcheck(final String id, final String productid,boolean writeable){
        if (writeable) {
            AlertDialog.Builder Abuilder = new AlertDialog.Builder(aCtivity);
            View vview = aCtivity.getLayoutInflater().inflate(R.layout.doyouwannawrite, null);
            final Button yes = vview.findViewById(R.id.dyww_yes);
            final Button no = vview.findViewById(R.id.dyww_no);
            Abuilder.setView(vview);
            final AlertDialog dialog = Abuilder.create();
            dialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    AlertDialog.Builder Abuilder2 = new AlertDialog.Builder(aCtivity);
                    View vview2 = aCtivity.getLayoutInflater().inflate(R.layout.nameoncake, null);
                    Button continue1 = vview2.findViewById(R.id.noc_continue);
                    Button cancel = vview2.findViewById(R.id.noc_cancel);
                    final EditText edittext = vview2.findViewById(R.id.caketext);

                    Abuilder2.setView(vview2);
                    final AlertDialog dialog2 = Abuilder2.create();
                    dialog2.show();
                    continue1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = edittext.getText().toString();
                            if (text.equals("")) {
                                Toast.makeText(aCtivity.getApplicationContext(), "Enter text to continue", Toast.LENGTH_SHORT).show();

                            } else {
                                dialog2.dismiss();
                                String data = "{" +
                                        "\"customerid\"" + ":" + "\"" + id + "\"," +
                                        "\"quantity\"" + ":" + "\"" + 1 + "\"," +
                                        "\"size\"" + ":" + "\"" + "" + "\"," +
                                        "\"sellerid\"" + ":" + "\"" + Sellerid + "\"," +
                                        "\"caketext\"" + ":" + "\"" + text + "\"," +
                                        "\"product\"" + ":" + "\"" + productid + "\"" +
                                        "}";
                                Submit(data);
                            }


                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog2.dismiss();

                        }
                    });
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    String text = "";
                    String data = "{" +
                            "\"customerid\"" + ":" + "\"" + id + "\"," +
                            "\"quantity\"" + ":" + "\"" + 1 + "\"," +
                            "\"size\"" + ":" + "\"" + "" + "\"," +
                            "\"sellerid\"" + ":" + "\"" + Sellerid + "\"," +
                            "\"caketext\"" + ":" + "\"" + text + "\"," +
                            "\"product\"" + ":" + "\"" + productid + "\"" +
                            "}";
                    Submit(data);

                }
            });
        } else {
            String text = "";
            String data = "{" +
                    "\"customerid\"" + ":" + "\"" + id + "\"," +
                    "\"quantity\"" + ":" + "\"" + 1 + "\"," +
                    "\"size\"" + ":" + "\"" + "" + "\"," +
                    "\"sellerid\"" + ":" + "\"" + Sellerid + "\"," +
                    "\"caketext\"" + ":" + "\"" + text + "\"," +
                    "\"product\"" + ":" + "\"" + productid + "\"" +
                    "}";
            Submit(data);
        }
    }

    private void showwarning(final String id, final String productid, final boolean writeable){
        AlertDialog.Builder Abuilder = new AlertDialog.Builder(aCtivity);
        View vview = aCtivity.getLayoutInflater().inflate(R.layout.cartalert, null);
        final Button ok = vview.findViewById(R.id.cartalertok);
        final Button cancel = vview.findViewById(R.id.cartalertcancel);
        Abuilder.setView(vview);
        final AlertDialog dialog = Abuilder.create();
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cartctextcheck(id,productid,writeable);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void getcartdetails(String data, final String id, final String productid, final boolean writeable) {
        final LoadingDialog pd = new LoadingDialog(aCtivity);
        final String savedata = data;
        String URL = checkcarturl;
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RQ = Volley.newRequestQueue(aCtivity.getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(mfragment.getRetainInstance());

        pd.setContentView(R.layout.dialog);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String responsecode =  objres.getString("responsecode");
                    if(responsecode.equals("1")){
                        String seller = objres.getString("sellerid");
                        sameseller = seller.equals(Sellerid);
                        if(!sameseller){
                            showwarning(Customerid,productid,writeable);
                        }
                        else {
                            cartctextcheck(Customerid,productid,writeable);
                        }
                    }else if(responsecode.equals("2")){
                        sameseller=true;
                        cartctextcheck(Customerid,productid,writeable);
                    }
                    else {
                        Toast.makeText(aCtivity.getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(aCtivity.getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        RQ.add(stringRequest);
    }
//    String loginapiurl = "http://dpend.pythonanywhere.com/accounts/Loginapi/";

}
