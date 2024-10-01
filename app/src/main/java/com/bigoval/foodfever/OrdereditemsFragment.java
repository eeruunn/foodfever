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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

public class OrdereditemsFragment extends Fragment implements OrderedItemAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private OrderedItemAdapter mAdapter;
    private ArrayList<OrderedItem> mExampleList;
    private RequestQueue mRequestQueue;
    private TextView noorders;
    Fragment frg;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    RequestQueue rq;
    DatabaseHelper dbhelper;
    String customerid;
    String r;
    String viewordersurl="http://dpend.pythonanywhere.com/products/orderlist/";
    String deletefromcarturl="http://dpend.pythonanywhere.com/products/deletefromcart/";
    String addtocarturl="http://dpend.pythonanywhere.com/products/addtocart/";
    String imageurl="http://dpend.pythonanywhere.com/media/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.ordered_items_fragment, container, false);
        if(!isNetworkAvailable()){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();

        }
        rq = Volley.newRequestQueue(getActivity());
        dbhelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String details[] = {"loggedin"};
        final Cursor c = db.query("login", details, null, null, null, null, null);
        c.moveToFirst();
        String id[] = {"id"};
        Cursor c2 = db.query("user_data", id, null, null, null, null, null);
        c2.moveToFirst();
        if (c.getInt(0) == 1) {
            try {
                customerid = c2.getString(0);
                String data = "{" +
                        "\"customerid\"" + ":" + "\"" + customerid + "\"" +
                        "}";
                Submit(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mRecyclerView = mview.findViewById(R.id.ordered_items_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());

        requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
        noorders = mview.findViewById(R.id.noorderstext);
        return mview;
    }

    private void Submit(String data) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = viewordersurl;

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() == 0){
                        noorders.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject data = jsonArray.getJSONObject(i);
                            OrderedItem item = new OrderedItem();
                            item.setImageUrl(imageurl + data.getString("image"));
                            item.setprice(data.getInt("price"));
                            item.setseller(data.getString("seller"));
                            item.setname(data.getString("name"));
                            item.setorderedon(data.getString("orderedon"));
                            item.setstatus(data.getString("status"));
                            item.setitemid(data.getString("orderid"));
                            item.setquantity(data.getString("quantity"));
                            item.settext(data.getString("orderdescription"));
                            mExampleList.add(item);


                        } catch (JSONException e) {
                            System.out.println(e);
                            Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                        }
                    }
                    mAdapter = new OrderedItemAdapter(getActivity().getApplicationContext(), mExampleList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(OrdereditemsFragment.this);
                } catch (JSONException e) {
                    e.printStackTrace();
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
        requestQueue.add(stringRequest);
    }


    private void Submit2(String data) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String ordercancelURL="http://dpend.pythonanywhere.com/products/orderlist/cancelorder/";

        pd.show();
        pd.setCanceledOnTouchOutside(getRetainInstance());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ordercancelURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject objres = new JSONObject(response);
                    String responsemessage = objres.getString("response");
                    String responsecode = objres.getString("responsecode");
                    if(responsecode.equals("0")){
                        Toast.makeText(getActivity().getApplicationContext(), "ordercancelled successfully", Toast.LENGTH_LONG).show();
                        frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), responsemessage, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

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
        requestQueue2.add(stringRequest);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void oncancelclick(int position) {
        OrderedItem item = mExampleList.get(position);
        String status = item.getstatus();
        if (!status.equals("cancelled")){
            String orderid = item.getitemid();
            String data = "{" +
                    "\"orderid\"" + ":" + "\"" + orderid + "\"," +
                    "\"customerid\"" + ":" + "\"" + customerid + "\"" +
                    "}";
            Submit2(data);

        }

    }
}
