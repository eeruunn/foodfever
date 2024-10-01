package com.bigoval.foodfever;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class OrderAddressFragment extends Fragment implements OrderAddressAdapter.OnItemClickListener {
    private Button addaddressbtn;
    private RecyclerView mRecyclerView;
    private OrderAddressAdapter mAdapter;
    private ArrayList<AddressViewItem> mExampleList;
    private RequestQueue mRequestQueue;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    RequestQueue rq;
    Boolean many;
    Bundle bundle0;
    DatabaseHelper dbhelper;
    String regphone;
    String r;
    String productseller;
    String size;
    String productname;
    String productprice;
    String productimage;
    String productquantity;
    String productid;
    String caketext;
    String addressurl = "http://dpend.pythonanywhere.com/accounts/address/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.orderaddressfragment, container, false);
        if(!isNetworkAvailable()){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();

        }
        bundle0 = this.getArguments();
        many = bundle0.getBoolean("many");
        if (!many) {
            productseller = bundle0.getString("seller");
            productimage = bundle0.getString("imageurl");
            caketext = bundle0.getString("caketext");
            productname = bundle0.getString("name");
            productprice = bundle0.getString("price");
            size = bundle0.getString("size");
            productquantity = bundle0.getString("quantity");
            productid = bundle0.getString("id");
        }
        addaddressbtn = mview.findViewById(R.id.order_add_addressbtn);
        addaddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Addaddresstypefragment()).addToBackStack(null).commit();
            }
        });
        rq = Volley.newRequestQueue(getActivity());
        dbhelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String details[] = {"loggedin"};
        final Cursor c = db.query("login", details, null, null, null, null, null);
        c.moveToFirst();
        String phone[] = {"phone"};
        Cursor c2 = db.query("user_data", phone, null, null, null, null, null);
        c2.moveToFirst();
        if (c.getInt(0) == 1) {
            try {
                regphone = c2.getString(0);
                String data = "{" +
                        "\"regphone\"" + ":" + "\"" + regphone + "\"" +
                        "}";
                Submit(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mRecyclerView = mview.findViewById(R.id.addressrecycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());

        return mview;
    }

    private void Submit(String data) {
        final String savedata = data;
        String URL = addressurl;
        final LoadingDialog ld = new LoadingDialog(getActivity());
        ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ld.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ld.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject data = jsonArray.getJSONObject(i);
                            AddressViewItem address = new AddressViewItem();
                            address.setAddress(data.getString("address1").toString());
                            address.setHousename(data.getString("housename").toString());
                            address.setLandmark(data.getString("landmark"));
                            address.setPhone(data.getString("mobilenumber"));
                            address.setname(data.getString("name"));
                            address.setPincode(data.getString("pincode"));
                            address.setTownname(data.getString("townname"));
                            address.setId(data.getString("id"));

                            mExampleList.add(address);


                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                        }
                    }
                    mAdapter = new OrderAddressAdapter(getActivity().getApplicationContext(), mExampleList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(OrderAddressFragment.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ld.dismiss();
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


    @Override
    public void onSelect(int position) {
        AddressViewItem item = mExampleList.get(position);


        String name = item.getname();
        String pincode = item.getPincode();
        String housename = item.getHousename();
        String phone = item.getPhone();
        String townname = item.getTownname();
        String address = item.getAddress();
        String landmark = item.getLandmark();
        if (many) {
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("pincode", pincode);
            bundle.putString("housename", housename);
            bundle.putString("phone", phone);
            bundle.putString("townname", townname);
            bundle.putString("address", address);
            bundle.putString("landmark", landmark);
            bundle.putBoolean("many", true);
            CheckoutFragment fragment = new CheckoutFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        } else {
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("pincode", pincode);
            bundle.putString("housename", housename);
            bundle.putString("phone", phone);
            bundle.putString("townname", townname);
            bundle.putString("address", address);
            bundle.putString("landmark", landmark);
            bundle.putBoolean("many", false);
            bundle.putString("productname", productname);
            bundle.putString("productimage", productimage);
            bundle.putString("seller", productseller);
            bundle.putString("price", productprice);
            bundle.putString("quantity", productquantity);
            bundle.putString("id", productid);
            bundle.putString("size", size);
            bundle.putString("caketext",caketext);
            CheckoutFragment fragment = new CheckoutFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
