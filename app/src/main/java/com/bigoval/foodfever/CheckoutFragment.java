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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CheckoutFragment extends Fragment {
    int totalprice = 0;
    Button placeorderbtn;
    TextView itemsprice;
    TextView deliverycharge;
    TextView totalorderprice;
    TextView oaddress;

    int t;
    int dcharge = 0;
    private RecyclerView mRecyclerView;
    Fragment frg;
    private Checkoutadapter mAdapter;
    private ArrayList<CartItem> mExampleList;
    private RequestQueue mRequestQueue;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    RequestQueue rq;
    DatabaseHelper dbhelper;
    String customerid;
    String r;
    Boolean many;
    String imageaddress = "http://dpend.pythonanywhere.com/media/";
    String orderurl = "http://dpend.pythonanywhere.com/products/order/";
    String carturl = "http://dpend.pythonanywhere.com/products/viewcart/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.checkoutfragment, container, false);
        if(!isNetworkAvailable()){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ErrorFragment()).addToBackStack(null).commit();

        }

        final Bundle bundle = this.getArguments();
        String pname = bundle.getString("productname");
        rq = Volley.newRequestQueue(getActivity());
        dbhelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String details[] = {"loggedin"};
        final Cursor c = db.query("login", details, null, null, null, null, null);
        c.moveToFirst();
        String id[] = {"id"};
        Cursor c2 = db.query("user_data", id, null, null, null, null, null);
        c2.moveToFirst();
        many = bundle.getBoolean("many");


        mRecyclerView = mview.findViewById(R.id.checkout_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());

        requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());

        final String checkname = bundle.getString("name");
        final String checkpin = bundle.getString("pincode");
        final String checkphone = bundle.getString("phone");
        final String checkhousename = bundle.getString("housename");
        final String checktownname = bundle.getString("townname");
        final String checklandmark = bundle.getString("landmark");
        final String checkaddress = bundle.getString("address");
        final String size = bundle.getString("size");


        oaddress = mview.findViewById(R.id.selectedaddressaddress);
        itemsprice = mview.findViewById(R.id.itemsprice);
        totalorderprice = mview.findViewById(R.id.totalorderprice);
        deliverycharge = mview.findViewById(R.id.deliverycharge);
        placeorderbtn = mview.findViewById(R.id.placeorderbtn);
        oaddress.setText(checkaddress);
        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (many) {
                    String url = "http://dpend.pythonanywhere.com/products/order/";
                    String data = "{" +
                            "\"customerid\"" + ":" + "\"" + customerid + "\"," +
                            "\"customerpincode\"" + ":" + "\"" + checkpin + "\"," +
                            "\"customeraddress\"" + ":" + "\"" + checkaddress + "\"," +
                            "\"customerlandmark\"" + ":" + "\"" + checklandmark + "\"," +
                            "\"customerhousename\"" + ":" + "\"" + checkhousename + "\"," +
                            "\"customertownname\"" + ":" + "\"" + checktownname + "\"," +
                            "\"customerphone\"" + ":" + "\"" + checkphone + "\"," +
                            "\"customername\"" + ":" + "\"" + checkname + "\"" +
                            "}";
                    Submit2(data, url);
                } else {
                    String url = "http://dpend.pythonanywhere.com/products/ordersingle/";
                    String data = "{" +
                            "\"customerid\"" + ":" + "\"" + customerid + "\"," +
                            "\"customerpincode\"" + ":" + "\"" + checkpin + "\"," +
                            "\"customeraddress\"" + ":" + "\"" + checkaddress + "\"," +
                            "\"customerlandmark\"" + ":" + "\"" + checklandmark + "\"," +
                            "\"customerhousename\"" + ":" + "\"" + checkhousename + "\"," +
                            "\"customertownname\"" + ":" + "\"" + checktownname + "\"," +
                            "\"customerphone\"" + ":" + "\"" + checkphone + "\"," +
                            "\"customername\"" + ":" + "\"" + checkname + "\"," +
                            "\"size\"" + ":" + "\"" + size + "\"," +
                            "\"quantity\"" + ":" + "\"" + bundle.getString("quantity") + "\"," +
                            "\"price\"" + ":" + "\"" + bundle.getString("price") + "\"," +
                            "\"id\"" + ":" + "\"" + bundle.getString("id") + "\"," +
                            "\"orderdescription1\"" + ":" + "\"" + bundle.getString("caketext") + "\"," +
                            "\"seller\"" + ":" + "\"" + bundle.getString("seller") + "\"" +
                            "}";
//                    System.out.println(bundle.getString("productname"));
                    Submit2(data, url);
                }


            }
        });
        if (many) {
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
        } else {
            if (c.getInt(0) == 1) {
                try {
                    customerid = c2.getString(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            totalprice = 0;
            String productname = bundle.getString("productname");
            System.out.println(productname);
            String productimage = bundle.getString("productimage");
            System.out.println(bundle.getString("productimage"));
            String seller = bundle.getString("seller");
            String price = bundle.getString("price");
            String quantity = bundle.getString("quantity");
            String productid = bundle.getString("id");
            CartItem item = new CartItem();
            item.setImageUrl(productimage);
            System.out.println(productimage);
            item.setprice(Integer.parseInt(price));
            item.setquantity(Integer.parseInt(quantity));
            item.setseller(seller);
            item.setname(productname);
            item.setid(productid);
            totalprice += Integer.parseInt(price) * Integer.parseInt(quantity);
            System.out.println(totalprice);
            mExampleList.add(item);
            mAdapter = new Checkoutadapter(getActivity().getApplicationContext(), mExampleList);
            mRecyclerView.setAdapter(mAdapter);
            dcharge = 30;

            int t = totalprice + dcharge;

            itemsprice.setText("₹" + totalprice);
            totalorderprice.setText("₹" + t);
            deliverycharge.setText("₹" + dcharge);

        }
        return mview;
    }

    private void Submit(String data) {
        totalprice = 0;
        final String savedata = data;
        String URL = carturl;

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject data = jsonArray.getJSONObject(i);
                            CartItem item = new CartItem();
                            item.setImageUrl(imageaddress + data.getString("image"));
                            item.setprice(data.getInt("price"));
                            item.setquantity(data.getInt("quantity"));
                            item.setseller(data.getString("seller"));
                            item.setname(data.getString("name"));
                            item.setid(data.getString("id"));
                            item.setdeliverablewithin(data.getInt("deliverablewithin"));
                            totalprice += data.getInt("price") * data.getInt("quantity");
                            System.out.println(totalprice);
                            mExampleList.add(item);


                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                        }
                    }
                    mAdapter = new Checkoutadapter(getActivity().getApplicationContext(), mExampleList);
                    mRecyclerView.setAdapter(mAdapter);
                    if(totalprice < 700){
                        dcharge = 30;
                    }
                    int t = totalprice + dcharge;

                    itemsprice.setText("₹" + totalprice);
                    totalorderprice.setText("₹" + t);
                    deliverycharge.setText("₹" + dcharge);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    private void Submit2(String data, String url) {
        final LoadingDialog pd = new LoadingDialog(getActivity());
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String savedata = data;
        String URL = url;

        requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    System.out.println(response);
                    JSONObject objres = new JSONObject(response);
                    String responsecode = objres.getString("responsecode");
                    if(responsecode.equals("0")){
                        Toast.makeText(getActivity().getApplicationContext(), "Order successfull", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity().getApplicationContext(), "Thank you for shopping with us", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorry not enough stocks left. Try to decrease quantity", Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    System.out.println(e);
                    Toast.makeText(getActivity().getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                System.out.println(error);
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
}
