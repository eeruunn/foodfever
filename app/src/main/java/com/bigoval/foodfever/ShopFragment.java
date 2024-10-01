package com.bigoval.foodfever;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ShopFragment extends Fragment {
    private TextView shopname;
    private TextView shopaddress;
    private ImageView shopbanner;
    private RequestQueue requestQueue;
    private RecyclerView menucatrecyclerview;
    private ArrayList<Menucatsitem> Menucatsitems;
    private MenuCatsAdapter menuCatsAdapter;
    String menuurl = "http://dpend.pythonanywhere.com/products/fetchmenu/";
    String id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_login, container, false);
        View mview = inflater.inflate(R.layout.shopfragment, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        shopbanner=mview.findViewById(R.id.shopfragment_shopbanner);
        shopname=mview.findViewById(R.id.shopfragment_shopname);
        shopaddress=mview.findViewById(R.id.shopfragment_shopaddress);
        menucatrecyclerview=mview.findViewById(R.id.shopmenurecyclerview);
        menucatrecyclerview.setHasFixedSize(true);
        menucatrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle = this.getArguments();
        Menucatsitems = new ArrayList<>();
        id = bundle.getString("id");
        String name = bundle.getString("name");
        String banner = bundle.getString("banner");
        String address = bundle.getString("address");
        shopname.setText(name);
        shopaddress.setText(address);
        String data = "{" +
                "\"seller\"" + ":" + "\"" + id + "\"" +
                "}";
        Submit(data);
        try{
            Picasso.get().load(banner).fit().centerInside().into(shopbanner);
        }catch (Exception e){
            e.printStackTrace();
        }

        return mview;
    }

    private void Submit(String data) {
        final String savedata = data;
        String URL =menuurl;
        final LoadingDialog dialog = new LoadingDialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject cat = jsonArray.getJSONObject(i);

                            Menucatsitem item = new Menucatsitem();
                            item.setCatname(cat.getString("catname"));
                            item.setProductsarray(cat.getJSONArray("products"));

                            System.out.println(cat.getJSONArray("products"));
                            Menucatsitems.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                        }
                    }
                    menuCatsAdapter = new MenuCatsAdapter(getActivity().getApplicationContext(), Menucatsitems,getFragmentManager(),ShopFragment.this,getActivity(),getActivity().getSharedPreferences("LoggedinData", Context.MODE_PRIVATE),id);
                    menucatrecyclerview.setAdapter(menuCatsAdapter);
//                    mExampleAdapter.setOnItemClickListener(SearchResultFragment.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
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


}
